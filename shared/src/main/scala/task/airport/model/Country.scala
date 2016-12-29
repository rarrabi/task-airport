package task.airport
package model

case class Country(id: Country.Id,
                   code: Country.Code,
                   name: String,
                   continent: Country.Continent,
                   wikipedia: URL,
                   keywords: Seq[String]) {
  require(name.nonEmpty)
}

object Country {

  case class Id(value: Long)

  case class Code(value: String) {
    require(value.nonEmpty)
  }

  object Code {

    implicit val codeOrdering: Ordering[Code] =
      Ordering by (_.value)

  }

  case class Continent(value: String) {
    require(value.nonEmpty)
  }

  implicit val countryOrdering: Ordering[Country] =
    Ordering by (_.code)

}
