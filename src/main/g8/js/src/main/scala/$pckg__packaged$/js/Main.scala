package $pckg;format="lower,package"$

import org.scalajs.dom
import scalatags.JsDom.all.*

import upickle.default.*
import kind.logic.js.mermaid._
import kind.logic.js.goldenlayout._

import org.scalajs.dom.HTMLDivElement
import org.scalajs.dom.HTMLElement
import kind.logic.*
import kind.logic.js.*
import kind.logic.telemetry.*
import kind.logic.js.scenarios.*
import kind.logic.js.svg.*
import scala.scalajs.js.Dynamic.global
import scala.util.control.NonFatal

object MainPage {

  lazy val svgContainer    = initSvg()
  lazy val mermaidPage     = initMermaid()
  lazy val scenarioBuilder = ScenarioBuilder()

  // init SVG
  private def initSvg() = {
    val appSvgContainer = div().render
    EventBus.activeTestScenario.subscribe { scenario =>
      def fallback = div(s"We couldn't parse the scenario as a DraftContract or Restaurant").render
      pizzaAsSvg(scenario).getOrElse(fallback)

      appSvgContainer.innerHTML = ""
      appSvgContainer.appendChild(pizzaAsSvg(scenario).getOrElse(fallback))

    }
    appSvgContainer
  }

  private def initMermaid() = {
    val appMermaidPage = MermaidPage()

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

        appMermaidPage.update(scenario, mermaidMarkdown)
      } catch {
        case NonFatal(e) =>
          appMermaidPage.updateError(
            scenario,
            s"We couldn't parse the scenario as a MakePizzaRequest: \$e"
          )
      }
    }

    appMermaidPage
  }

  def pizzaAsSvg(scenario: TestScenario): Option[HTMLDivElement] = {
    try {
      val request                = read[MakePizzaRequest](scenario.input)
      given telemetry: Telemetry = Telemetry()
      PizzaOps.defaultProgram.orderPizza(request.quantity, request.toppings).execOrThrow()
      val calls = telemetry.calls.execOrThrow()
      Option(SvgForCalls(calls))
    } catch {
      case NonFatal(e) =>
        println(s"Error creating svg: \$e")
        None
    }
  }

}
case class MakePizzaRequest(quantity: Int, toppings: List[String]) derives ReadWriter {
  def toJson = writeJs(this)
}

@scala.scalajs.js.annotation.JSExportTopLevel("initLayout")
def initLayout(myLayout: GoldenLayout) = {

  val drawer = HtmlUtils.\$[HTMLElement]("drawer")

  myLayout.addMenuItem(drawer, "Scenario Builder") { state =>
    MainPage.scenarioBuilder.content
  }

  myLayout.addMenuItem(drawer, "Diagram") { state =>
    MainPage.mermaidPage.element
  }

  myLayout.addMenuItem(drawer, "SVG") { state =>
    MainPage.svgContainer
  }

  //
  EventBus.activeTabs.subscribe { activeTabs =>
    // if the UIComponent isn't shown, then we should remove the menu item
    UIComponent.inactiveComoponents().foreach(_.showMenuItem())
    activeTabs.foreach(_.hideMenuItem())
  }

  myLayout.init()
}

@main
def mainJSApp(): Unit = {
  global.window.initLayout = initLayout
  global.window.createNewComponent = createNewComponent
  global.window.onComponentCreated = onComponentCreated
  global.window.onComponentDestroyed = onComponentDestroyed
}
