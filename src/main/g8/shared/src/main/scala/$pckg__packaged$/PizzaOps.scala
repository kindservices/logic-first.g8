package $pckg;format="lower,package"$

import kind.logic.*
import kind.logic.telemetry.*
import PizzaLogic.*
import PizzaOperation.*

object PizzaOps {

  def defaultProgram(using telemetry: Telemetry) = PizzaApp {
    [A] =>
      (command: PizzaOperation[A]) =>
        command match {
          case command @ SaveOrder(_, _) => 1.asResultTraced(Actor.database("app", "DB"), command)
          case command @ Bake(toppings) =>
            Pizza(toppings).asResultTraced(Actor.service("app", "Kitchen"), command)
          case command @ Deliver(pizza) =>
            (12.34).asResultTraced(Actor.service("app", "Delivery"), command)
          case command @ RecordOrder(id, money) =>
            ().asResultTraced(Actor.service("app", "Accounts"), command)
      }
  }

  def asMermaid(quantity: Int, toppings: List[String]): String = {
    given telemetry: Telemetry = Telemetry()
    defaultProgram.orderPizzaAsMermaid(quantity, toppings)._2
  }
}
