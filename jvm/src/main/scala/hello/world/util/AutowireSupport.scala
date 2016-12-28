package hello.world.util

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import autowire.Server
import de.heikoseeberger.akkahttpupickle.UpickleSupport._
import upickle.Js
import upickle.default.{Reader, Writer}

trait AutowireSupport {

  val server: Server[Js.Value, Reader, Writer] =
    new Server[Js.Value, Reader, Writer] {

      override def read[Result: Reader](json: Js.Value): Result =
        implicitly[Reader[Result]] read json

      override def write[Result: Writer](result: Result): Js.Value =
        implicitly[Writer[Result]] write result

    }

  def auto(router: server.type => server.Router): Route =
    path("ajax" / Segments) { segments =>
      entity(as[server.Request]) { request =>
        complete {
          router(server)(request)
        }
      }
    }

}

object AutowireSupport extends AutowireSupport
