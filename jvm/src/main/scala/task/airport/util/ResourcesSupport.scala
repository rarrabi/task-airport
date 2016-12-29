package task.airport
package util

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

trait ResourcesSupport {

  val resources: Route =
    getFromResourceDirectory("")

}

object ResourcesSupport extends ResourcesSupport
