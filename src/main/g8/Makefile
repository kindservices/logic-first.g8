generateModels:
	source ./genModels.sh && genModels
mermaid:
	docker run --rm -v \${PWD}/diagrams:/data minlag/mermaid-cli mmdc -i /data/sequence.md -o /data/sequence.png
c4:
	docker run -it --rm -p 8090:8080 -v \${PWD}/diagrams:/usr/local/structurizr structurizr/lite