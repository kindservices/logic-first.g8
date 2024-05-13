package $pckg;format="lower,package"$


import kind.logic.*
import scala.util.Try

object PizzaLogic {

  case class Pizza(toppings: List[String])

  type Money = Double

  enum PizzaOperation[A]:
    case SaveOrder(quantity: Int, toppings: List[String]) extends PizzaOperation[Int]
    case Bake(toppings: List[String])                     extends PizzaOperation[Pizza]
    case Deliver(pizza: Pizza)                            extends PizzaOperation[Money]
    case RecordOrder(orderId: Int, money: Money)          extends PizzaOperation[Unit]

  object PizzaOperation:
    import PizzaOperation.*

    def apply(quantity: Int, toppings: List[String]): Program[PizzaOperation, Pizza] = {
      for {
        orderId <- SaveOrder(quantity, toppings).asProgram
        pizza   <- Bake(toppings).asProgram
        money   <- Deliver(pizza).asProgram
        _       <- RecordOrder(orderId, money).asProgram
      } yield pizza
    }

}

