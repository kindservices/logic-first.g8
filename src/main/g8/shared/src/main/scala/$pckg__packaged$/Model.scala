package

import scala.language.implicitConversions
import kind.logic.json.*
import kind.logic.*
import kind.logic.telemetry.Telemetry
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import zio.*

object model {
  given t: Telemetry = Telemetry()

  // here is a basic, typical flow where somebody saves some data and then does a query
  val data = "some input"
  val testCase = for
    _ <- (data.asTask *> BFF.save(data).traceWith(Action(UI, BFF.System, "onSave"), data))
    .traceWith(Action(Admin, UI, "save form"), data)
  queryApi    = Search()
  queryString = "the quick brown fox"
  userQuery <- (queryString.asTask *> queryApi
    .query(queryString)
    .traceWith(Action(UI, Service, "query"), queryString))
    .traceWith(Action(Admin, UI, "do a search"), queryString)
  mermaid <- t.mermaid
  c4      <- t.c4
  yield (mermaid.diagram(), c4.diagram())

  val (mermaidDiagram, c4) = testCase.execOrThrow()
  import eie.io.{*, given}
  println(t.pretty)
  "sequence.md".asPath.text = mermaidDiagram
  "workspace.dsl".asPath.text = c4
}