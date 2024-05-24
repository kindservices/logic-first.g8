package $pckg;format="lower,package"$

import $pckg;format="lower,package"$.PizzaHandler.InMemory
import $pckg;format="lower,package"$.PizzaLogic.PizzaOperation._

import kind.logic._
import kind.logic.telemetry._

import PizzaLogic._

case class Services(handler: PizzaHandler.InMemory, app: PizzaApp.App) {
  def orderPizza(quantity: Int, toppings: List[String]): Pizza = {
    app.orderPizza(quantity, toppings).execOrThrow()
  }
}

object Services {

  @scala.scalajs.js.annotation.JSExportTopLevel("createService")
  def createService(): Services = {
    val (impl, appLogic) = PizzaApp.inMemory(using Telemetry()).execOrThrow()
    Services(impl, appLogic)
  }

}