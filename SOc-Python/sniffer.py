from datetime import datetime
import time
from collections import defaultdict, deque
import requests
from scapy.all import sniff, IP, TCP, UDP, ICMP

EVENTS_URL = "http://localhost:8080/api/security-events"
METRICS_URL = "http://localhost:8080/api/metrics"

# Configuration
PORT_SCAN_THRESHOLD = 10
TIME_WINDOW = 10

# State Tracking
port_tracker = defaultdict(list)
total_packets = 0
connected_devices = set()
packet_times = deque()

INTERESTING_PORTS = {
    21: "FTP", 22: "SSH", 23: "TELNET", 25: "SMTP",
    53: "DNS", 80: "HTTP", 110: "POP3", 139: "NETBIOS",
    443: "HTTPS", 445: "SMB", 3389: "RDP"
}

def detect_port_scan(source_ip, destination_ip, destination_port):
    current_time = time.time()

    # Track this connection attempt
    port_tracker[source_ip].append((current_time, destination_ip, destination_port))

    # Prune elements outside the sliding time window
    port_tracker[source_ip] = [
        entry for entry in port_tracker[source_ip]
        if current_time - entry[0] <= TIME_WINDOW
    ]

    # Count unique target ports targeted by this source IP
    unique_ports = set(entry[2] for entry in port_tracker[source_ip])
    port_count = len(unique_ports)

    # Check if the unique port count crosses our scan threshold
    if port_count >= PORT_SCAN_THRESHOLD:
        if port_count >= 50:
            severity = "CRITICAL"
        elif port_count >= 20:
            severity = "HIGH"
        else:
            severity = "MEDIUM"

        description = (
            f"Port scan detected: {source_ip} targeted {port_count} "
            f"different ports on the network within {TIME_WINDOW} seconds."
        )
        return True, port_count, severity, description

    return False, port_count, "LOW", None

def packet_callback(packet):
    global total_packets

    if not packet.haslayer(IP):
        return

    source_ip = packet[IP].src
    destination_ip = packet[IP].dst

    # -----------------------------
    # Network metrics
    # -----------------------------
    total_packets += 1
    connected_devices.add(source_ip)
    connected_devices.add(destination_ip)

    current_time = time.time()
    packet_times.append(current_time)

    # Remove packets older than 1 second
    while packet_times and current_time - packet_times[0] > 1:
        packet_times.popleft()

    packets_per_second = len(packet_times)
    print(
        f"Packets/sec: {packets_per_second} | "
        f"Total packets: {total_packets} | "
        f"Unique devices: {len(connected_devices)}", 
        end="\r" # Keeps terminal output clean
    )

    # 1. Dispatch Live Metrics (Send on every packet before filter loops)
    metrics = {
        "packetsPerSecond": packets_per_second,
        "totalPackets": total_packets,
        "uniqueDevices": len(connected_devices) # Fixed: Sent as a serializable number
    }
    
    try:
        requests.post(METRICS_URL, json=metrics, timeout=1) # Fast timeout to prevent blocking
    except requests.exceptions.RequestException:
        pass # Silently drop metric post errors to prevent lagging raw packet stream

    # -----------------------------
    # Protocol & Alert Parsing
    # -----------------------------
    protocol = "IP"
    event_type = None
    description = None
    severity = "LOW"
    
    is_scan = False
    port_count = 0

    # TCP Traffic
    if packet.haslayer(TCP):
        protocol = "TCP"
        destination_port = packet[TCP].dport
        
        is_scan, port_count, scan_sev, scan_desc = detect_port_scan(
            source_ip, destination_ip, destination_port
        )
        
        if is_scan:
            event_type = "PORT_SCAN"
            severity = scan_sev
            description = scan_desc
        elif destination_port in INTERESTING_PORTS:
            service = INTERESTING_PORTS[destination_port]
            event_type = "TCP_TRAFFIC"
            description = f"{service} traffic detected on port {destination_port}"

    # UDP Traffic
    elif packet.haslayer(UDP):
        protocol = "UDP"
        destination_port = packet[UDP].dport
        
        is_scan, port_count, scan_sev, scan_desc = detect_port_scan(
            source_ip, destination_ip, destination_port
        )
        
        if is_scan:
            event_type = "PORT_SCAN"
            severity = scan_sev
            description = scan_desc
        elif destination_port in INTERESTING_PORTS:
            service = INTERESTING_PORTS[destination_port]
            event_type = "UDP_TRAFFIC"
            description = f"{service} traffic detected on port {destination_port}"

    # ICMP Traffic
    elif packet.haslayer(ICMP):
        protocol = "ICMP"
        event_type = "ICMP_TRAFFIC"
        description = "ICMP traffic detected"

    # Ignore packets that do not trigger alerts
    if event_type is None:
        return

    # 2. Dispatch Security Alerts
    timestamp = datetime.now().isoformat()
    event = {
        "timestamp": timestamp,
        "sourceIp": source_ip,
        "destinationIp": destination_ip,
        "eventType": event_type,
        "protocol": protocol,
        "severity": severity,
        "description": description,
        "status": "OPEN"
    }

    try:
        response = requests.post(EVENTS_URL, json=event, timeout=2)
        print(
            f"\n[ALERT] {timestamp} | {source_ip} -> {destination_ip} | "
            f"{event_type} ({severity}) | HTTP {response.status_code}"
        )
    except requests.exceptions.RequestException as error:
        print(f"\n[ERROR] Could not send alert to Spring Boot backend: {error}")

def main():
    print("Starting packet sniffer...")
    print("Press Ctrl+C to stop.")
    try:
        sniff(prn=packet_callback, store=False)
    except KeyboardInterrupt:
        print("\nSniffer stopped by user.")

if __name__ == "__main__":
    main()
