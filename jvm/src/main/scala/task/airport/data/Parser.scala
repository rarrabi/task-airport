package task.airport
package data

import task.airport.model._

object Parser {

  private def booleanNoYes(value: String): Boolean = value match {
    case "no" => false
    case "yes" => true
  }

  private def booleanZeroOne(value: String): Boolean = value match {
    case "0" => false
    case "1" => true
  }

  private def option(value: String): Option[String] = value match {
    case "" => None
    case _ => Some(value)
  }

  def degree(value: String): Degree =
    Degree(value.toDouble)

  def feet(value: String): Feet =
    Feet(value.toInt)

  def url(value: String): URL =
    URL(value)

  private def keywords(value: String): Seq[String] =
    value split "," map (_.trim)

  def country(values: Seq[String]): Country = values match {
    case Seq(id, code, name, continent, wikipedia, keywords) =>
      Country(
        id = Country.Id(id.toInt),
        code = Country.Code(code),
        name = name,
        continent = Country.Continent(continent),
        wikipedia = Parser url wikipedia,
        keywords = Parser keywords keywords
      )
  }

  def airport(values: Seq[String]): Airport = values match {
    case Seq(id, identifier, tpe, name, latitude, longitude, elevation, continent, country, region, municipality, scheduledService, gpsCode, iataCode, localCode, home, wikipedia, keywords) =>
      Airport(
        id = Airport.Id(id.toLong),
        identifier = Airport.Identifier(identifier),
        tpe = tpe,
        name = name,
        latitude = Parser degree latitude,
        longitude = Parser degree longitude,
        elevation = Parser option elevation map Parser.feet,
        continent = Country.Continent(continent),
        country = Country.Code(country),
        region = region,
        municipality = Parser option municipality,
        scheduledService = Parser booleanNoYes scheduledService,
        gpsCode = Parser option gpsCode,
        iataCode = Parser option iataCode,
        localCode = Parser option localCode,
        home = Parser option home map Parser.url,
        wikipedia = Parser option wikipedia map Parser.url,
        keywords = Parser keywords keywords
      )
  }

  def runway(values: Seq[String]): Runway = values match {
    case Seq(id, airport, airportIdentifier, length, width, surface, lighted, closed, details@_*) if details.size == 2 * 6 =>
      val le = details take 6
      val he = details drop 6
      Runway(
        id = Runway.Id(id.toLong),
        airport = Airport.Id(airport.toLong),
        airportIdentifier = Airport.Identifier(airportIdentifier),
        length = Parser option length map Parser.feet,
        width = Parser option width map Parser.feet,
        surface = Parser option surface map (Runway.Surface(_)),
        lighted = Parser booleanZeroOne lighted,
        closed = Parser booleanZeroOne closed,
        le = Parser runwayDetailsOption le,
        he = Parser runwayDetailsOption he
      )
  }

  private def runwayDetailsOption(values: Seq[String]): Option[Runway.Details] =
    Parser option values.head map (_ => Parser runwayDetails values)

  private def runwayDetails(values: Seq[String]): Runway.Details = values match {
    case Seq(identifier, latitude, longitude, elevation, heading, displacedThreshold) =>
      Runway.Details(
        identifier = Runway.Details.Identifier(identifier stripPrefix "\"" stripSuffix "\""),
        latitude = Parser option latitude map Parser.degree,
        longitude = Parser option longitude map Parser.degree,
        elevation = Parser option elevation map Parser.feet,
        heading = Parser option heading map (_.toDouble),
        displacedThreshold = Parser option displacedThreshold map Parser.feet
      )
  }

}
