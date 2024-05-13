package $pckg;format="lower,package"$

import org.scalajs.dom
import scalatags.JsDom.all.*
import org.scalajs.dom.{HTMLElement, Node, document, html}

import scala.scalajs.js.Dynamic.global
import scala.concurrent.Future
import scala.concurrent.duration.given
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSExportTopLevel
import upickle.default.*

import kind.logic.js.*
import kind.logic.telemetry.*
import scala.util.control.NonFatal


case class MakePizzaRequest(quantity: Int, toppings: List[String]) derives ReadWriter {
  def toJson = writeJs(this)
}

def initMermaid() = {
  LocalState.addDefaultScenario(
    TestScenario(
      "Pizza Happy Path",
      "",
      MakePizzaRequest(1, List("cheese", "tomato sauce", "pepperoni")).toJson
    )
  )

  EventBus.activeTestScenario.subscribe { scenario =>
    try {
      val request = read[MakePizzaRequest](scenario.input)

      val mermaidMarkdown = PizzaOps.asMermaid(request.quantity, request.toppings)

      val cleaned = mermaidMarkdown
        .replace("```mermaid", "")
        .replace("```", "")
        .trim

      AppSkeleton.mermaidPage.update(scenario, cleaned)
    } catch {
      case NonFatal(e) =>
        AppSkeleton.mermaidPage.updateError(scenario, s"We couldn't parse the scenario as a DraftContract: \$e")
    }
  }
}

@main
def mainJSApp(): Unit = {
  initMermaid()

  new Drawer(HtmlUtils.\$("drawer")).refresh()

  global.window.createScenarioBuilder = createScenarioBuilder
  global.window.createSequenceDiagram = createSequenceDiagram
  global.window.createInteractivePage = createInteractivePage
  global.window.createDiffPage = createDiffPage

  global.window.onComponentDestroyed = onComponentDestroyed
  global.window.onComponentCreated = onComponentCreated
}
