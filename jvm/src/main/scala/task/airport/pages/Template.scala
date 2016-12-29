package task.airport
package pages

import scalatags.Text.all._

object Template {

  def render(bodyFrag: => Frag): Tag =
    html(
      head(
        script(src := assets.jQuery),
        script(src := resources.taskAirportJS)
      ),
      body(
        bodyFrag
      )
    )

}
