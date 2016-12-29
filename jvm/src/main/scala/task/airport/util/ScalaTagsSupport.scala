package task.airport
package util

import akka.http.scaladsl.marshalling.Marshaller.StringMarshaller
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.MediaTypes.`text/html`

import scalatags.Text.Tag

// See: https://github.com/cretz/scala-web-ideal/blob/master/server/src/main/scala/webideal/util/ScalaTagsSupport.scala
trait ScalaTagsSupport {

  implicit val ScalaTagsMarshaller: ToEntityMarshaller[Tag] =
    (StringMarshaller wrap `text/html`) {
      case html if html.tag == "html" => s"<!DOCTYPE html>${html.render}"
      case tag => tag.render
    }

}

object ScalaTagsSupport extends ScalaTagsSupport
