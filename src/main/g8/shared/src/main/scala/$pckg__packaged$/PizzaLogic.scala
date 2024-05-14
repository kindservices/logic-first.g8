package $pckg;format="lower,package"$


import kind.logic.*
import kind.logic.telemetry.*
import PizzaLogic.*
import PizzaOperation.*
import zio.*

/** The 'Handler' provides in implementation for our logic.
  *
  * You don't *need* to use this trait -- you could just implement the logic directly in your
  * PizzaApp.
  *
  * @param orderDb
  * @param financeDB
  */
trait PizzaHandler(val orderDb: OrderDB, val financeDB: FinanceDB) {

  def impl(using telemetry: Telemetry): [A] => PizzaOperation[A] => Result[A] = {
    [A] =>
      (_: PizzaOperation[A]) match {
        case command @ SaveOrder(_, _) =>
          orderDb.save(command).taskAsResultTraced(Actor.database("app", "DB"), command)
        case command @ Bake(toppings) =>
          Pizza(toppings).asResultTraced(Actor.service("app", "Kitchen"), command)
        case command @ Deliver(pizza) =>
          val price = 10 + (pizza.toppings.size * 1.75)
          price.asResultTraced(Actor.service("app", "Delivery"), command)
        case command @ RecordOrder(id, money) =>
          financeDB.save(command).taskAsResultTraced(Actor.service("app", "Accounts"), command)

    }
  }
}

object PizzaHandler {

  class InMemory(val telemetry: Telemetry, ord: OrderDB.InMemory, fin: FinanceDB.InMemory)
      extends PizzaHandler(ord, fin) {
    def implementation = impl(using telemetry)
  }

  def inMemory(using telemetry: Telemetry): UIO[PizzaHandler.InMemory] = {
    for {
      orderDB   <- OrderDB.inMemory()
      financeDB <- FinanceDB.inMemory()
    } yield new InMemory(telemetry, orderDB, financeDB)
  }

}

trait OrderDB {
  def save(save: PizzaOperation.SaveOrder): Task[Int]
}
object OrderDB {
  final case class SavedOrder(id: Int, quantity: Int, toppings: List[String])
  case class InMemory(orderDB: Ref[Map[Int, SavedOrder]]) extends OrderDB {
    override def save(data: PizzaOperation.SaveOrder): Task[Int] = {
      orderDB.modify { db =>
        val id    = db.size + 1
        val newDb = db + (id -> SavedOrder(id, data.quantity, data.toppings))
        (id, newDb)
      }
    }
  }

  def inMemory(): UIO[OrderDB.InMemory] = {
    for {
      orderDB <- Ref.make(Map.empty[Int, SavedOrder])
    } yield InMemory(orderDB)
  }
}

trait FinanceDB {
  def save(save: PizzaOperation.RecordOrder): Task[Unit]
}
object FinanceDB {

  case class InMemory(ref: Ref[Seq[PizzaOperation.RecordOrder]]) extends FinanceDB {
    override def save(data: PizzaOperation.RecordOrder) = {
      ref.update { db =>
        data +: db
      }
    }
  }

  def inMemory(): UIO[FinanceDB.InMemory] = {
    for {
      db <- Ref.make(Seq.empty[PizzaOperation.RecordOrder])
    } yield FinanceDB.InMemory(db)
  }
}
