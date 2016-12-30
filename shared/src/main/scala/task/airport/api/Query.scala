package task.airport
package api

import task.airport.model.{Airport, Country, Runway}

trait Query {

  def searchCountry(text: String): Option[(Country, Seq[(Airport, Seq[Runway])])]

}
