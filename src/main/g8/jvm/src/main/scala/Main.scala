import $pckg;format="lower,package"$.*

import scala.language.implicitConversions
import kind.logic.json.*
import kind.logic.*
import kind.logic.telemetry.Telemetry
import zio.*

object Main {
  def main(args : Array[String]) : Unit = {
    given t: Telemetry = Telemetry()

    val (mermaidDiagram, c4) = scenarios.example("some input").execOrThrow()
    import eie.io.{*, given}
    println(t.pretty)

    // a basic mermaid sequence diagram
    val sequence = "diagrams/sequence.md".asPath
    sequence.text = mermaidDiagram
    println(s"Created \${sequence}")

    // for C4 diagrams
    val c4File = "diagrams/workspace.dsl".asPath
    c4File.text = c4
    println(s"Created \${c4File}")
  }
}