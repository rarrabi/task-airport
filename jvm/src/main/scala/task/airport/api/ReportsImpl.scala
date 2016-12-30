package task.airport
package api

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import task.airport.impl.Flows
import task.airport.model.{Country, Runway}

import scala.collection.immutable.{TreeMap, TreeSet}
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

case class ReportsImpl(timeout: Duration)(implicit materializer: Materializer) extends Reports {

  object flows {

    lazy val airportCounts: Source[(Seq[(Country, Int)], Seq[(Country, Int)]), NotUsed] =
      Flows.airportsByCountry
        .mapConcat {
          _ mapValues (_.size)
        }
        .fold {
          TreeMap.empty[Int, Seq[Country]]
        } { case (map, (country, airportCount)) =>
          map + (airportCount -> (map.getOrElse(airportCount, Seq.empty[Country]) :+ country))
        }
        .map { countryByAirportCount =>
          val topCountryByAirportCount = countryByAirportCount take 10 mapValues (_.sorted)
          val bottomCountryByAirportCount = countryByAirportCount takeRight 10 mapValues (_.sorted)
          val topAirportCountByCountry = topCountryByAirportCount.toSeq flatMap { case (count, countries) => countries map (_ -> count) }
          val bottomAirportCountByCountry = bottomCountryByAirportCount.toSeq flatMap { case (count, countries) => countries map (_ -> count) }
          (topAirportCountByCountry, bottomAirportCountByCountry)
        }
        .named("airportCounts")

    lazy val runwaySurfaces: Source[Seq[(Country, Seq[Runway.Surface])], NotUsed] =
      Flows.runwaysByCounty
        .mapConcat {
          _ mapValues (_ flatMap (_.surface))
        }
        .fold {
          TreeMap.empty[Country, TreeSet[Runway.Surface]]
        } { case (map, (country, surfaces)) =>
          map + (country -> (map.getOrElse(country, TreeSet.empty[Runway.Surface]) ++ surfaces))
        }
        .map { surfacesByCountry =>
          (surfacesByCountry mapValues (_.toSeq)).toSeq
        }
        .named("runwaySurfaces")

    lazy val runwayIdentifications: Source[Seq[Runway.Details.Identifier], NotUsed] =
      Flows.runwaysWithDetails
        .map {
          _._2.identifier
        }
        .fold {
          TreeSet.empty[Runway.Details.Identifier]
        }(_ + _)
        .map { runwayIdentifications =>
          (runwayIdentifications take 10).toSeq
        }
        .named("runwayIdentifications")

  }

  object futures {

    def airportCounts: Future[(Seq[(Country, Int)], Seq[(Country, Int)])] =
      flows.airportCounts
        .runWith(Sink.head)

    def runwaySurfaces: Future[Seq[(Country, Seq[Runway.Surface])]] =
      flows.runwaySurfaces
        .runWith(Sink.head)

    def runwayIdentifications: Future[Seq[Runway.Details.Identifier]] =
      flows.runwayIdentifications
        .runWith(Sink.head)

  }

  override def airportCounts: (Seq[(Country, Int)], Seq[(Country, Int)]) =
    Await.result(futures.airportCounts, timeout)

  override def runwaySurfaces: Seq[(Country, Seq[Runway.Surface])] =
    Await.result(futures.runwaySurfaces, timeout)

  override def runwayIdentifications: Seq[Runway.Details.Identifier] =
    Await.result(futures.runwayIdentifications, timeout)

}
