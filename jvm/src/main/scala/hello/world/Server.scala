package hello.world

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import hello.world.util.AutowireSupport.auto
import hello.world.util.ResourceSupport.resources
import hello.world.util.ScalaTagsSupport.ScalaTagsMarshaller
import hello.world.util.WebJarsSupport.webJars
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn

object Server extends App {

  sys.props += "org.slf4j.simpleLogger.logFile" -> "System.out"
  sys.props += "org.slf4j.simpleLogger.defaultLogLevel" -> "DEBUG"

  private val log = LoggerFactory getLogger getClass

  log.debug("Initializing")
  implicit val system = ActorSystem("HelloWorld")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  log.info(s"Initialized: $system")

  implicit val exception =
    ExceptionHandler {
      case throwable: Throwable =>
        throw throwable
    }

  val route =
    get {
      pathSingleSlash {
        complete {
          Pages.index
        }
      } ~
        resources ~
        webJars
    } ~
      post {
        auto(_ route[HelloWorld] HelloWorldImpl)
      }

  log.debug("Starting")
  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
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
