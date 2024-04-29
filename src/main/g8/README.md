# $name$ Logic-First Project

This project was generated from the [kindservices/logic-first.g8](https://github.com/kindservices/logic-first.g8) template.

See [Kind Services](https://www.kindservices.co.uk/) for more on a Logic-First approach to executable architecture.

# Building

## Data Models

An intial `service.yaml` was generated for a contract-first approach to REST services.

The accompanying `Makefile` can generate scala (JVM and JS) model files from that service by running:

```sh
make packageRestCode
```

That will locally pubish the model files, which can then be used from your project.
(Be sure to uncomment the lines in [build.sbt](build.sbt) if would like to use this contract-first open-api approach in your project)

## Compiling

This project is built using [sbt](https://www.scala-sbt.org/):

```sh
sbt ~compile
```

Or, for a zero-install (but likely much slower) docker build:

```sh
docker run -it --rm -v \${PWD}:/app -w /app hseeberger/scala-sbt:8u222_1.3.5_2.13.1 sbt compile
```