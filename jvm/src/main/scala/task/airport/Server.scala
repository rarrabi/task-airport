package task.airport

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpupickle.UpickleSupport._
import task.airport.api.{QueryImpl, ReportsImpl}
import task.airport.model.{Airport, Country, Runway}
import task.airport.util.ResourcesSupport._
import task.airport.util.ScalaTagsSupport._
import task.airport.util.WebJarsSupport._
import upickle.default.macroRW

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn

object Server extends App {

  Logging.initialize()
  private val log = Logging logger getClass

  log.debug("Initializing")
  val configuration = Configuration load "task.airport"
  implicit val system = ActorSystem(name = "TaskAirport")
  implicit val materializer = ActorMaterializer(namePrefix = Some("task-airport-flow"))
  implicit val executionContext = system.dispatcher
  log.info(s"Initialized: $system")
  log.debug(s"Configuration: $configuration")

  // Akka HTTP doesn't log the stack trace by default. See: ExceptionHandler.default
  val exceptionHandler = ExceptionHandler {
    case throwable: Throwable =>
      extractRequest { request =>
        // It might be dangerous to log the HTTP Request.
        log.error(s"Failed to process request: $request", throwable)
        throw throwable
      }
  }

  implicit val rw = macroRW[Country] merge macroRW[Airport] merge macroRW[Runway]
  val query = QueryImpl(timeout = 5.seconds)
  val reports = ReportsImpl(timeout = 5.seconds)

  val handler =
    handleExceptions(exceptionHandler) {
      get {
        pathSingleSlash {
          complete {
            pages.Index.render
          }
        } ~ pathPrefix(pages.paths.resources) {
          resources
        } ~ pathPrefix(pages.paths.assets) {
          webJars
        } ~ pathPrefix("api") {
          pathPrefix("query") {
            (path("searchCountry") & parameter('name.as[String])) { name =>
              onSuccess(query.futures searchCountry name)(complete(_))
            }
          } ~ pathPrefix("reports") {
            path("airportCounts") {
              onSuccess(reports.futures.airportCounts) tapply (complete(_))
            } ~ path("runwaySurfaces") {
              onSuccess(reports.futures.runwaySurfaces)(complete(_))
            } ~ path("runwayIdentifications") {
              onSuccess(reports.futures.runwayIdentifications)(complete(_))
            }
          }
        }
      } ~ post {
        pathPrefix("api") {
          pathPrefix("reports") {
            path("airportCounts") {
              onSuccess(reports.futures.airportCounts) tapply (complete(_))
            }
          }
        }
      }
    }

  log.debug("Starting")
  val bindingFuture = Http().bindAndHandle(handler, configuration.interface, configuration.port)
  val binding = Await.result(bindingFuture, 1.seconds)
  log.info(s"Started: $binding")

  println("Press RETURN to shut down...")
  StdIn.readLine()

  log.debug("Stopping")
  val terminatedFuture =
    bindingFuture
      .flatMap(_.unbind())
      .flatMap(_ => system.terminate())
  val terminated = Await.result(terminatedFuture, 1.seconds)
  log.info(s"Stopped: $terminated")

}
