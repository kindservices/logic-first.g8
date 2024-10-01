package $pckg;format="lower,package"$

import scala.language.implicitConversions
import kind.logic.json.*
import kind.logic.*
import kind.logic.telemetry.Telemetry
import zio.*

/**
 * This is just a basic example of an n-tier app.
 *
 * Feel free to model your application any way you like.
 *
 * The important part is to leverate the zio 'Task' type by adding '.traceWith(...)' to capture the caller/recipient
 */
object model {

  val UI: Container       = Container.webApp("App", "UI")
  val Service: Container  = Container.service("App", "Backend")
  val Database: Container = Container.database("App", "PostgreSQL")

  val Admin: Container = Container.person("Acme", "Admin")

  // this is an example of just a naked function - some operation we annotate w/ our service
  def saveData(data: String)(using telemetry: Telemetry) = {
    given System: Container = Service
    data.hashCode().asTask.traceWith(Action.calls(Database), data)
  }

  // this is an example of a basic object (not a trait) where we can just put some functions
  object BFF {
    given System: Container = Container.service
    def save(data: String)(using telemetry: Telemetry) =
    saveData(data).traceWith(Action.calls(Service), data)
  }

  // this is a more typical example of defining an interface for a service with a companion object
  trait Search {
    def query(term: String): Task[Seq[Search.Result]]
  }
  object Search {
    // just some made-up type. Here we have a map of words to their length
    type Result = Map[String, Int]

    // this could be a micro-service, but here we'll make search the responsibility of the same B/E service
    given System: Container = Service

    def apply()(using telemetry: Telemetry): Search = new Search {
      override def query(term: String): Task[Seq[Search.Result]] = {
        val parts = term.split(" ").toSeq
        for result <- ZIO.foreachPar(parts) { word =>
          Map(word -> word.length).asTask.traceWith(Action.calls(Database), word)
        }
        yield result
      }
    }
  }

}