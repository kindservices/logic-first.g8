package $pckg;format="lower,package"$

import scala.language.implicitConversions
import kind.logic.json.*
import kind.logic.*
import kind.logic.telemetry.Telemetry
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import zio.*

object scenarios {

  def example(input : String)(using t: Telemetry) = {
    for
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
  }
}