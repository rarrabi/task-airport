package task.airport

package object pages {

  object paths {

    val resources = "resources"
    val assets = "assets"

    def resource(path: String): String =
      s"/$resources/$path"

    def asset(path: String): String =
      s"/$assets/$path"

  }

  //noinspection TypeAnnotation
  object resources {

    val taskAirportJS = paths resource "task-airport-fastopt.js"
    val taskAirportJSLauncher = paths resource "task-airport-launcher.js"

  }

  //noinspection TypeAnnotation
  object assets {

    val jQuery = paths asset "jquery/jquery.js"

  }

}
