package hello.world.util

import autowire.Client
import org.scalajs.dom
import upickle.Js
import upickle.default.{Reader, Writer}

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

trait AutowireSupport {

  def client(path: String): Client[Js.Value, Reader, Writer] =
    new Client[Js.Value, Reader, Writer] {

      override def doCall(request: Request): Future[Js.Value] =
        dom.ext.Ajax.post(
          url = s"$path/${request.path mkString "/"}",
          headers = Map("Content-Type" -> "application/json"),
          data = upickle.json write write(request)
        ) map (upickle.json read _.responseText)

      override def read[Result: Reader](json: Js.Value): Result =
        implicitly[Reader[Result]] read json

      override def write[Result: Writer](result: Result): Js.Value =
        implicitly[Writer[Result]] write result

    }

  val proxy: Client[Js.Value, Reader, Writer] =
    client("/ajax")

}

object AutowireSupport extends AutowireSupport
