package hello.world.util

import akka.http.scaladsl.marshalling.Marshaller.StringMarshaller
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.MediaTypes.`text/html`

import scalatags.Text.TypedTag

// See: https://github.com/cretz/scala-web-ideal/blob/master/server/src/main/scala/webideal/util/ScalaTagsSupport.scala
trait ScalaTagsSupport {

  implicit val ScalaTagsMarshaller: ToEntityMarshaller[TypedTag[String]] =
    scalaTagsMarshaller[String]

  def scalaTagsMarshaller[Output <: String]: ToEntityMarshaller[TypedTag[Output]] =
    (StringMarshaller wrap `text/html`) {
      case html@TypedTag("html", _, _) => s"<!DOCTYPE html>${html.render}"
      case tag => tag.render
    }

}

object ScalaTagsSupport extends ScalaTagsSupport
