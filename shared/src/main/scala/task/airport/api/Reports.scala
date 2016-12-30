package task.airport
package api

import task.airport.model.{Country, Runway}

trait Reports {

  def airportCounts: (Seq[(Country, Int)], Seq[(Country, Int)])

  def runwaySurfaces: Seq[(Country, Seq[Runway.Surface])]

  def runwayIdentifications: Seq[Runway.Details.Identifier]

}
