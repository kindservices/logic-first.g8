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

import kind.logic.*
import kind.logic.js.*
import kind.logic.telemetry.*
import kind.logic.js.svg.*

import scala.util.control.NonFatal
import org.scalajs.dom.HTMLDivElement


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
        AppSkeleton.mermaidPage.updateError(scenario, s"We couldn't parse the scenario as a MakePizzaRequest: \$e")
    }
  }
}


def pizzaAsSvg(scenario: TestScenario): Option[HTMLDivElement] = {
  try {
    val request                = read[MakePizzaRequest](scenario.input)
    given telemetry: Telemetry = Telemetry()
    val result =
      PizzaOps.defaultProgram.orderPizza(request.quantity, request.toppings).execOrThrow()
    val calls = telemetry.calls.execOrThrow()
    Option(SvgForCalls(calls))
  } catch {
    case NonFatal(e) =>
      println(s"Error creating svg: \$e")
      None
  }
}

def initSvg() = {
  EventBus.activeTestScenario.subscribe { scenario =>
    def fallback  = div(s"We couldn't parse the scenario as a DraftContract or Restaurant").render
    val component = pizzaAsSvg(scenario).getOrElse(fallback)
    AppSkeleton.svgPage.innerHTML = ""
    AppSkeleton.svgPage.appendChild(component)
  }
}


@main
def mainJSApp(): Unit = {
  initMermaid()
  initSvg()

  new Drawer(HtmlUtils.\$("drawer")).refresh()

  global.window.createScenarioBuilder = createScenarioBuilder
  global.window.createSequenceDiagram = createSequenceDiagram
  global.window.createInteractivePage = createInteractivePage
  global.window.createDiffPage = createDiffPage

  global.window.onComponentDestroyed = onComponentDestroyed
  global.window.onComponentCreated = onComponentCreated
}
