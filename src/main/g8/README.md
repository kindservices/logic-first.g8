# $name$ Logic-First Project

This project was generated from the [kindservices/logic-first.g8](https://github.com/kindservices/logic-first.g8) template.

[Logic-First](https://github.com/kindservices/logic-first) is a [Kind](https://www.kindservices.co.uk/) project created 
to streamline application development by deriving the architecture from working code.

The process is:

 * Start with the code - the data types, models, and systems and interfaces
 * Annotate those calls with tracing data, and run test scenarios through the system
 * Use Your Models:
   * Use the telemetry from those example/test scenarios to generate your diagrams:
     * sequence diagrams
     * C4 diagrams
   * Use the working code 
     * package (either a .jar file for JVM services or .js files for typescript/javascript) the code, brought in as a dependency by specific services (e.g. BFFs, UIs or back-ends which actually talk to databases)
 
## How to use it?


### C4

[C4](https://c4model.com/) is 

To open the C4 diagram

```sh
docker run -it --rm -p 8090:8080 -v ${PWD}:/usr/local/structurizr structurizr/lite
```

# Building

The accompanying [Makefile](./Makefile) has:

### generateModels - create REST stubs
`make generateModels` will iterate through all the (./schemas)[./schemas] subdirectories, expecing a `service.yml` and `openapi-config.yml` in each.

It will generate and the package up the scala stubs for each so that they can be used within this project in both the JVM and JS projects

To add a new service/generated data model, just copy and modify the existing (example)[./schemas/example]
