# $name$ Logic-First Project

This project was generated from the [kindservices/logic-first.g8](https://github.com/kindservices/logic-first.g8) template.

See [Kind Services](https://www.kindservices.co.uk/) for more on a Logic-First approach to executable architecture.

# Building

This project is built using [sbt](https://www.scala-sbt.org/):

```sh
sbt ~compile
```

Or, for a zero-install docker build:

```sh
docker run -it --rm -v \${PWD}:/app -w /app hseeberger/scala-sbt:11.0.15_1.7.2_2.13.10 sbt compile
```