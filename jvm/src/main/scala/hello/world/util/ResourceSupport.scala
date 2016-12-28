package hello.world.util

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

trait ResourceSupport {

  val resources: Route =
    getFromResourceDirectory("")

}

object ResourceSupport extends ResourceSupport
