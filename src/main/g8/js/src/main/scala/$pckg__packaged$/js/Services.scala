package $pckg;format="lower,package"$

import $pckg;format="lower,package"$.PizzaHandler.InMemory
import $pckg;format="lower,package"$.PizzaLogic.PizzaOperation._

import kind.logic._
import kind.logic.telemetry._

import scala.scalajs.js.annotation._
import scala.scalajs.js

import PizzaLogic._

@JSExportTopLevel("Services")
@JSExportAll
case class Services(handler: PizzaHandler.InMemory, app: PizzaApp.App) {

  def db = handler.orderDb

  def orderPizza(quantity: Int, toppings: js.Array[String]): Pizza = {
    app.orderPizza(quantity, toppings.toList).execOrThrow()
  }
}

object Services {

  @JSExportTopLevel("createService")
  def createService(): Services = {
    val (impl, appLogic) = PizzaApp.inMemory(using Telemetry()).execOrThrow()
    Services(impl, appLogic)
  }

}