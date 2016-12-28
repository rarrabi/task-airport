package hello.world.util

import akka.http.scaladsl.marshalling.Marshaller.StringMarshaller
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.MediaTypes.`text/css`

import scalacss.Defaults
import scalacss.internal.mutable.StyleSheet
import scalacss.internal.{Env, Renderer}

// See: https://github.com/cretz/scala-web-ideal/blob/master/server/src/main/scala/webideal/util/ScalaCssSupport.scala
trait ScalaCSSSupport {

  implicit val ScalaCSSMarshaller: ToEntityMarshaller[StyleSheet.Base] =
    scalaCSSMarshaller(Defaults.cssStringRenderer, Defaults.cssEnv)

  def scalaCSSMarshaller(implicit renderer: Renderer[String], env: Env): ToEntityMarshaller[StyleSheet.Base] =
    (StringMarshaller wrap `text/css`) (_.render)

}

object ScalaCSSSupport extends ScalaCSSSupport
