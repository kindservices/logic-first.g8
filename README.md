# Logic-First Template

This is a [Giter8](http://www.foundweekends.org/giter8/index.html) template for creating a new logic-first project.

# Motivation

Logic-first is about taking the [agile manifesto](https://agilemanifesto.org/) "Working software over comprehensive documentation" to the next level.

Rather than starting with architecture diagrams which need to be kept in-sync with the working software, logic-first 
generates architecture diagrams _from_ working software. This way you:

 1) eliminate drift: architecture HAS to be in-sync with code. If the code changes, the diagrams change
 2) have diagrams for every scenario - not just 'happy path'
 3) tighten the feedback-loop between design, implementation and data (i.e. test scenarios)
 4) decouple the logic from specifics. logic-first projects are about APIs, design and data-flows. The actual writing to databases/putting on queues/etc is done in a separate project, the same as with 'contract-first' development  

## Usage

To create a new project using this template, run:

```bash
sbt new kindservices/logic-first.g8
```

Or, if you don't have [sbt](https://www.scala-sbt.org/) installed:

```sh
docker run -it --rm -v ${PWD}:/app -w /app hseeberger/scala-sbt:8u222_1.3.5_2.13.1 sbt new kindservices/logic-first.g8
```


And fill in the prompts (or accept the defaults), then follow the generated README.md.


## Working on this template:

If you have coursier installed, you can just run the `test.sh` file. 

Otherwise, run:
```sh
which cs || (brew install coursier/formulas/coursier && cs setup)
which g8 || cs install giter8
g8 file://
```
