package task.airport
package util

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.webjars.WebJarAssetLocator

import scala.util.{Failure, Success, Try}

// See: https://github.com/ThoughtWorksInc/akka-http-webjars/blob/master/src/main/scala/com/thoughtworks/akka/http/WebJarsSupport.scala
trait WebJarsSupport {

  private val webJarAssetLocator = new WebJarAssetLocator

  val webJars: Route =
    path(Segment / Remaining) { (webJar, partialPath) =>
      Try {
        webJarAssetLocator.getFullPath(webJar, partialPath)
      } match {
        case Success(fullPath) => getFromResource(fullPath)
        case Failure(_: IllegalArgumentException) => reject
        case Failure(exception) => failWith(exception)
      }
    }

}

object WebJarsSupport extends WebJarsSupport
