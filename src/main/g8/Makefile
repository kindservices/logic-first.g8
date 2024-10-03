build:
	mill _.run
watch:
	mill --watch _.run
generateModels:
	source ./genModels.sh && genModels
sequence: build
	docker run --rm -v \${PWD}/diagrams:/data minlag/mermaid-cli mmdc -i /data/sequence.md -o /data/sequence.png
c4: build
	docker run -it --rm -p 8090:8080 -v \${PWD}/diagrams:/usr/local/structurizr structurizr/lite