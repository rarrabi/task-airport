package task.airport
package api

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import task.airport.data.Data
import task.airport.impl.{Flows, _}
import task.airport.model.{Airport, Country, Runway}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

case class QueryImpl(timeout: Duration)(implicit materializer: Materializer) extends Query {

  object flows {

    private val countryLookup: Source[Map[String, Country], NotUsed] =
      Data.countries
        .map { country =>
          country.code.value -> country
        }
        .concat {
          Data.countries
            .mapConcat { country =>
              prefixes(country.name) map (normalize(_) -> country)
            }
        }
        .foldMap

    def searchCountry(text: String): Source[Option[(Country, Seq[(Airport, Seq[Runway])])], NotUsed] =
      countryLookup
        .flatMapConcat { textToCountry =>
          Flows.airportsAndRunwaysByCountry
            .map { countryToAirportAndRunways =>
              textToCountry get normalize(text) flatMap { country =>
                countryToAirportAndRunways get country map (country -> _.toSeq)
              }
            }
        }
        .named("searchCountry")

  }

  object futures {

    def searchCountry(text: String): Future[Option[(Country, Seq[(Airport, Seq[Runway])])]] =
      flows.searchCountry(text)
        .runWith(Sink.head)

  }

  override def searchCountry(text: String): Option[(Country, Seq[(Airport, Seq[Runway])])] =
    Await.result(futures.searchCountry(text), timeout)

  private def prefixes(name: String): List[String] = {
    val prefix = name take 2
    val suffix = name drop 2
    val result = suffix.scanLeft(prefix)(_ + _)
    result.toList
  }

  private def normalize(text: String) =
    text.toLowerCase

}
