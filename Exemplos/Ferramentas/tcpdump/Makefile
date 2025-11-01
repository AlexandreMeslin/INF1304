COMPOSE = docker compose
ALICE   = $$(docker ps -qf "name=alice")
BOB     = $$(docker ps -qf "name=bob")
SNIFFER = $$(docker ps -qf "name=sniffer")

# Sobe o ambiente
up:
	$(COMPOSE) up -d

# Para e remove tudo
down:
	$(COMPOSE) down -v

# Abre shell no Alice
alice:
	docker exec -it $(ALICE) bash

# Abre shell no Bob
bob:
	docker exec -it $(BOB) bash

# Abre shell no Sniffer
sniffer:
	docker exec -it $(SNIFFER) bash

# Alice pinga Bob
ping:
	docker exec $(ALICE) ping -c 4 172.28.0.3

# Sniffer captura ICMP
icmp:
	docker exec -it $(SNIFFER) tcpdump -i any icmp

# Sniffer captura ARP
arp:
	docker exec -it $(SNIFFER) tcpdump -i any arp

# Sniffer captura HTTP em texto
http:
	docker exec -it $(SNIFFER) tcpdump -i any tcp port 80 -A

# Sniffer salva captura em arquivo
pcap:
	docker exec -it $(SNIFFER) tcpdump -i any -w /tmp/captura.pcap

# Copia o arquivo de captura para o host
get-pcap:
	docker cp $(SNIFFER):/tmp/captura.pcap ./captura.pcap
