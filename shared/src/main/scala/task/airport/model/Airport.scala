package task.airport
package model

case class Airport(id: Airport.Id,
                   identifier: Airport.Identifier,
                   tpe: String,
                   name: String,
                   latitude: Degree,
                   longitude: Degree,
                   elevation: Option[Feet],
                   continent: Country.Continent,
                   country: Country.Code,
                   region: String,
                   municipality: Option[String],
                   scheduledService: Boolean,
                   gpsCode: Option[String],
                   iataCode: Option[String],
                   localCode: Option[String],
                   home: Option[URL],
                   wikipedia: Option[URL],
                   keywords: Seq[String]) {
  require(tpe.nonEmpty)
  require(name.nonEmpty)
  require(region.nonEmpty)
  require(municipality forall (_.nonEmpty))
  require(gpsCode forall (_.nonEmpty))
  require(iataCode forall (_.nonEmpty))
  require(localCode forall (_.nonEmpty))
}

object Airport {

  case class Id(value: Long)

  case class Identifier(value: String) {
    require(value.nonEmpty)
  }

}
