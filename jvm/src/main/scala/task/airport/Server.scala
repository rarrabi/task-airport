package task.airport

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer

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

  val handler =
    handleExceptions(exceptionHandler) {
      get {
        pathSingleSlash {
          complete("index")
        } ~ path("query") {
          complete("query")
        } ~ path("reports") {
          complete("reports")
        } ~ path("assets") {
          complete("assets") // webjars
        } ~ path("resources") {
          complete("resources") // resources
        }
      } ~ post {
        path("api") {
          complete("api") // query, reports
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
