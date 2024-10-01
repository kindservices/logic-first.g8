# $name$ Logic-First Project

This project was generated from the [kindservices/logic-first.g8](https://github.com/kindservices/logic-first.g8) template.

[Logic-First](https://github.com/kindservices/logic-first) is a [Kind](https://www.kindservices.co.uk/) project created 
to streamline application development by deriving the architecture from working code.

The process is:

 * Start with the code - the data types, models, and systems and interfaces
 * Annotate those calls with tracing data, and run test scenarios through the system
 * Use Your Models:
   * Use the telemetry from those example/test scenarios to generate your diagrams:
     * [sequence diagrams](https://mermaid.js.org/syntax/sequenceDiagram.html)
     * [C4 diagrams](https://c4model.com/) (using [structurizr](https://docs.structurizr.com/))
   * Use the working code 
     * package (either a .jar file for JVM services or .js files for typescript/javascript) the code, brought in as a dependency by specific services (e.g. BFFs, UIs or back-ends which actually talk to databases)
 
## How to use it?

This example contains:

 - *Makefile*: convenient targets for generating diagrams, opening a C4 UI, generating models, etc 
 - *shared*: source code which should contain your data models APIs and service interfaces. This compiles to both
 - *jvm*: code specific to the JVM (e.g. for writing files to the filesystem)
 - *js*: code specific to the javascript platform (e.g. convenience methods which make working w/ scala types better)

It can be built with:

 * [sbt](https://www.scala-sbt.org/) : `sbt "project appJVM" run`
 * [mill](https://mill-build.org/mill/0.11.12/Java_Intro_to_Mill.html) : `mill _.run`

### C4

[C4](https://c4model.com/) is an approach to visualizing software architecture.
This project uses [structurizr](https://docs.structurizr.com/) as a means to easily visualize and navigate that architecture.

To open the C4 diagram, use `make c4` or just:

```sh
docker run -it --rm -p 8090:8080 -v \${PWD}:/usr/local/structurizr structurizr/lite
```

### Contract-First

Personally, I use 'logic-first' to quickly model my data types and systems.
Scala3 is uniquely qualified for this given its powerful but elegant type system (such as [opaque types](https://docs.scala-lang.org/scala3/book/types-opaque-types.html) or [intersection types](https://docs.scala-lang.org/scala3/book/types-intersection.html), etc)

Once those types and interfaces stabilise, I then move to [contract-first](https://swagger.io/resources/articles/adopting-an-api-first-approach/) (or "api-first") approach.
That is, I "promote" my local types into an openapi schema, and then use the resulting generated types in my models.

To help support this workflow, the accompanying [Makefile](./Makefile) has:

### generateModels - create REST stubs
`make generateModels` will iterate through all the (./schemas)[./schemas] subdirectories, expecting a `service.yml` and `openapi-config.yml` in each.

It will generate and the package up the scala stubs for each so that they can be used within this project in both the JVM and JS projects

To add a new service/generated data model, just copy and modify the existing (example)[./schemas/example]
