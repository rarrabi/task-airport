package task.airport
package pages

import scalatags.Text.all._

object Index {

  val render: Tag =
    Template.render(
      bodyFrag = Seq(
        h1("Airport"),
        div(id := "task-airport",
          h2("Query"),
          div(id := "task-airport-query"),
          h2("Reports"),
          div(id := "task-airport-reports")
        ),
        script(src := resources.taskAirportJSLauncher)
      )
    )

}
