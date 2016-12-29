package task.airport
package model

case class Runway(id: Runway.Id,
                  airport: Airport.Id,
                  airportIdentifier: Airport.Identifier,
                  length: Option[Feet],
                  width: Option[Feet],
                  surface: Option[Runway.Surface],
                  lighted: Boolean,
                  closed: Boolean,
                  le: Option[Runway.Details],
                  he: Option[Runway.Details]) {
  require(length forall (_.value >= 0))
  require(width forall (_.value >= 0))
}

object Runway {

  case class Id(value: Long)

  case class Surface(value: String) {
    require(value.nonEmpty)
  }

  object Surface {

    implicit val surfaceOrdering: Ordering[Surface] =
      Ordering by (_.value)

  }

  case class Details(identifier: Details.Identifier,
                     latitude: Option[Degree],
                     longitude: Option[Degree],
                     elevation: Option[Feet],
                     heading: Option[Double],
                     displacedThreshold: Option[Feet]) {
    require(heading forall (_ >= 0))
  }

  object Details {

    case class Identifier(value: String) {
      require(value.nonEmpty)
    }

    object Identifier {

      implicit val identifierOrdering: Ordering[Identifier] =
        Ordering by (_.value)

    }

  }

}