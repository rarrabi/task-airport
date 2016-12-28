package hello.world

import autowire._
import hello.world.util.AutowireSupport.proxy
import org.scalajs.{dom, jquery}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

object Client extends JSApp {

  @JSExport
  override def main(): Unit = {
    // DOM
    (dom.document getElementById "hello-world-js")
      .appendChild {
        val p = dom.document createElement "p"
        p.appendChild(
          dom.document createTextNode s"${Constants.HelloWorld} (JS, DOM)"
        )
        p
      }

    // JQuery
    (jquery jQuery "#hello-world-js")
      .append(
        (jquery jQuery "<p>")
          .append(
            s"${Constants.HelloWorld} (JS, jQuery)"
          )
      )

    // DOM + ScalaTags
    (dom.document getElementById "hello-world-js")
      .appendChild(
        p(
          s"${Constants.HelloWorld} (JS, DOM, ScalaTags)"
        ).render
      )

    // JQuery + ScalaTags
    (jquery jQuery "#hello-world-js")
      .append(
        p(
          s"${Constants.HelloWorld} (JS, jQuery, ScalaTags)"
        ).render
      )

    // Autowire, uPickle
    val helloWorld = proxy[HelloWorld]
    (helloWorld helloWorld "AJAX").call().foreach { result =>
      (jquery jQuery "#hello-world-js")
        .append(
          p(
            s"$result (Autowire, uPickle)"
          ).render
        )
    }

  }

}
