package task.airport

import java.net.URL

package object data {

  object paths {

    val resources = "task/airport"

    def resource(path: String): URL =
      getClass getResource s"/$resources/$path"

  }

  //noinspection TypeAnnotation
  object resources {

    val airportsCSV = paths resource "airports.csv"
    val countriesCSV = paths resource "countries.csv"
    val runwaysCSV = paths resource "runways.csv"

  }

}
