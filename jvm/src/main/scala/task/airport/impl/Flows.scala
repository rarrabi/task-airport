package task.airport
package impl

import akka.NotUsed
import akka.stream.scaladsl.Source
import task.airport.data.Data
import task.airport.model.{Airport, Country, Runway}

object Flows {

  // Lookup

  lazy val countryByCode: Source[Map[Country.Code, Country], NotUsed] =
    Data.countries
      .foldMapGroupBy(_.code)
      .named("countryByCode")

  lazy val countryByName: Source[Map[String, Country], NotUsed] =
    Data.countries
      .foldMapGroupBy(_.name)
      .named("countryByName")

  lazy val airportById: Source[Map[Airport.Id, Airport], NotUsed] =
    Data.airports
      .foldMapGroupBy(_.id)
      .named("airportById")

  lazy val countryByAirportId: Source[Map[Airport.Id, Country], NotUsed] =
    countriesWithAirports
      .foldMapWith { case (country, airport) => airport.id -> country }
      .named("countryByAirportId")

  lazy val airportsByCountry: Source[Map[Country, Seq[Airport]], NotUsed] =
    countriesWithAirports
      .foldMapSeq
      .named("airportsByCountry")

  lazy val runwaysByAirport: Source[Map[Airport, Seq[Runway]], NotUsed] =
    airportsWithRunways
      .foldMapSeq
      .named("runwaysByAirport")

  lazy val runwaysByCounty: Source[Map[Country, Seq[Runway]], NotUsed] =
    countriesWithRunways
      .foldMapSeq
      .named("runwaysByCounty")

  // Others

  lazy val runwaysWithDetails: Source[(Runway, Runway.Details), NotUsed] =
    Data.runways
      .mapConcat { runway =>
        List(runway.he, runway.le).flatten map (runway -> _)
      }
      .named("runwaysWithDetails")

  lazy val airportsAndRunwaysByCountry: Source[Map[Country, Map[Airport, Seq[Runway]]], NotUsed] =
    countryByCode
      .flatMapConcat { countries =>
        runwaysByAirport
          .map { airportToRunway =>
            airportToRunway map { case (airport, runways) =>
              countries(airport.country) -> (airportToRunway filterKeys (_ == airport))
            }
          }
      }
      .named("airportsAndRunwaysByCountry")

  // Helpers

  lazy val countriesWithAirports: Source[(Country, Airport), NotUsed] =
    countryByCode
      .joinConcat(Data.airports)(_.country)
      .named("countriesWithAirports")

  lazy val airportsWithRunways: Source[(Airport, Runway), NotUsed] =
    airportById
      .joinConcat(Data.runways)(_.airport)
      .named("airportsWithRunways")

  lazy val countriesWithRunways: Source[(Country, Runway), NotUsed] =
    countryByAirportId
      .joinConcat(Data.runways)(_.airport)
      .named("countriesWithRunways")

}
