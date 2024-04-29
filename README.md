# Logic-First Template

This is a [Giter8](http://www.foundweekends.org/giter8/index.html) template for creating a new logic-first project.

Logic-first is about taking the [agile manifesto](https://agilemanifesto.org/) "Working software over comprehensive documentation" to the next level.

## The Problem
Typically people do the intuitive thing and treat software like building a house: they draw up some architectural drawings from their idea/vision, and then give that to a team to construct.

Makes sense, right? Except software isn't physical construction. If you do that in software, you have to ask the question "does the software we wrote actually match the diagram?". You need to test it. You need to keep them both in-sync/up-to-date. And it's often vastly incomplete, only showing the "happy path". 

### It Gets Worse
Even if you DO manage to do this, microservice architectures involve separate teams. Are THEY doing it too? And how do their changes (and the changes to the wonderful common utility functions) impact other parts of the system? What's the chance any one person understands how it all hangs together, let along the whole team? What does that cost in terms of getting new team members up-to-speed? Now go on holiday for 2 weeks ... what does the system do when you get back?

People get locked into a frozen architecture because nobody can understand the whole system. Even if you did a good job originally when you separated out concerns, that lovely first release gets frozen in time, becoming accidental complexity costing you time and agility.

## The Solution: Use Logic-First for an executable architecture

Logic-First means starting with the one thing you really care about: your business logic and how it fits together.

It's a low-code, zero dependency approach where you represent your business logic as a data structure.

That pure data structure is your source of truth from which you derive your architecture, not the other way around.

Coupled with a contract-first approach to your schemas, this approach allows you to get [immediate feedback](https://www.youtube.com/watch?v=PUv66718DII) on your system and eliminates a massive source of miscommunication and waste.

Read more about Logic-First and get help upskilling your team to use it with [Kind Services here](https://kindservices.co.uk/).

# About this template

Logic-First is a combination of:
 * an approach (representing your program as a data structure)
 * a tech stack. You can use any (though some choices are better than others!). This one uses Scala for its powerful type-system, expressive syntax, and [the Cask OpenApi generator](https://github.com/OpenAPITools/openapi-generator/tree/master/samples/server/petstore/scala-cask]) for server code which compiles to javascript
 * pluggable interpreters -- the value multipliers for your logic as you turn it into sequence diagrams, svg animations, working code, .... whatever!

## Usage

To create a new project using this template, run:

```bash
sbt new kindservices/logic-first.g8
```

Or, if you don't have [sbt](https://www.scala-sbt.org/) installed:

```sh
docker run -it --rm -v ${PWD}:/app -w /app hseeberger/scala-sbt:8u222_1.3.5_2.13.1 sbt new kindservices/logic-first.g8
```


And fill in the promps (or accept the defaults), then follow the generated READMD.md.


## Working on this template:

If you have coursier installed, you can just run the `test.sh` file. 

Otherwise run:
```sh
which cs || (brew install coursier/formulas/coursier && cs setup)
which g8 || cs install giter8
g8 file://
```
