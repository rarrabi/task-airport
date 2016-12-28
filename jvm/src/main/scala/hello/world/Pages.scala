package hello.world

import scalatags.Text._
import scalatags.Text.all._

object Pages {

  val index: TypedTag[String] =
    html(
      head(
        script(src := "/assets/jquery/jquery.js"),
        script(src := "/scalajs-hello-world-fastopt.js")
      ),
      body(
        div(id := "hello-world-jvm",
          p(
            s"${Constants.HelloWorld} (JVM, ScalaTags)"
          )
        ),
        div(id := "hello-world-js"),
        script(src := "/scalajs-hello-world-launcher.js")
      )
    )

}
