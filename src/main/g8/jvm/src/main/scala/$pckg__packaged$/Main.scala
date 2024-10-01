package $pckg;format="lower,package"$

import scala.language.implicitConversions
import kind.logic.json.*
import kind.logic.*
import kind.logic.telemetry.Telemetry
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import zio.*

@main def genModels() = {
  given t: Telemetry = Telemetry()

  val (mermaidDiagram, c4) = scenarios.example("some input").execOrThrow()
  import eie.io.{*, given}
  println(t.pretty)

  // a basic mermaid sequence diagram
  "diagrams/sequence.md".asPath.text = mermaidDiagram

  // for C4 diagrams
  "diagrams/workspace.dsl".asPath.text = c4
}