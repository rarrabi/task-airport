package task.airport
package data

import java.net.URL

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.github.tototoshi.csv.CSVReader
import task.airport.model.{Airport, Country, Runway}

import scala.io.{Source => IOSource}
import scala.util.{Failure, Success, Try}

object Data {

  private val log = Logging logger getClass

  lazy val countries: Source[Country, NotUsed] =
    source(resources.countriesCSV)(Parser.country)

  lazy val airports: Source[Airport, NotUsed] =
    source(resources.airportsCSV)(Parser.airport)

  lazy val runways: Source[Runway, NotUsed] =
    source(resources.runwaysCSV)(Parser.runway)

  private def source[T](url: URL)(parse: Seq[String] => T): Source[T, NotUsed] =
    Source
      .fromIterator { () =>
        val source = IOSource fromURL url
        val reader = CSVReader open source
        val iterator = reader.iterator
        iterator
      }
      .drop(1)
      .map { row =>
        Try {
          parse(row)
        } match {
          case Success(value) =>
            value
          case Failure(exception) =>
            // It might be dangerous to log the CSV row.
            log.warn(s"Failed to parse: $row")
            throw exception
        }
      }

}
