package $pckg;format="lower,package"$

import scala.util.Try
import kind.logic.*
import kind.logic.telemetry.*
import zio.*

import PizzaLogic.*

trait PizzaApp {
  def orderPizza(quantity :Int, toppings : List[String]): Task[Pizza]
}

object PizzaApp {

  val Id = Actor.service[PizzaApp]

  class App(appLogic: [A] => PizzaOperation[A] => Result[A])(using telemetry: Telemetry)
      extends RunnableProgram[PizzaOperation](appLogic)
      with PizzaApp {

    override protected def appCoords = Id

    // I tried to lift this up to RunnableProgram, but apparently doing this with the generic F[_] type
    // was a bridget too far
    def withOverride(overrideFn: PartialFunction[PizzaOperation[?], Result[?]]): App = {
      val newLogic: [A] => PizzaOperation[A] => Result[A] = [A] => {
        (_: PizzaOperation[A]) match {
          case value if overrideFn.isDefinedAt(value) =>
            overrideFn(value).asInstanceOf[Result[A]]
          case value => logic(value).asInstanceOf[Result[A]]
        }
      }
      new App(newLogic)
    }

    override def orderPizza(quantity :Int, toppings : List[String]) = run(PizzaOperation(quantity, toppings))


    def orderPizzaAsMermaid(quantity: Int, toppings: List[String]) = {
      val result = orderPizza(quantity, toppings).execOrThrow()

      // get the call stack as a mermaid diagram
      val mermaid = telemetry.asMermaidDiagram().execOrThrow()
      result -> mermaid
    }
  }
  
  def apply(how: [A] => PizzaOperation[A] => Result[A])(using telemetry: Telemetry = Telemetry()) = new App(how)
}