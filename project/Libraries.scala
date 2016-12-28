import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

//noinspection TypeAnnotation
object Libraries {

  object shared {

    val cats = Def setting ("org.typelevel" %%% "cats" % "0.8.1")
    val scalactic = Def setting ("org.scalactic" %%% "scalactic" % "3.0.1")
    val scalaz = Def setting ("org.scalaz" %%% "scalaz-core" % "7.2.8")
    val scalazEffect = Def setting ("org.scalaz" %%% "scalaz-effect" % "7.2.8")
    val shapeless = Def setting ("com.chuusai" %%% "shapeless" % "2.3.2")
    val spire = Def setting ("org.spire-math" %%% "spire" % "0.13.0")

    val autowire = Def setting ("com.lihaoyi" %%% "autowire" % "0.2.6")
    val upickle = Def setting ("com.lihaoyi" %%% "upickle" % "0.4.4")

    val scalaTags = Def setting ("com.lihaoyi" %%% "scalatags" % "0.6.2")
    val scalaCss = Def setting ("com.github.japgolly.scalacss" %%% "core" % "0.5.1")
    val scalaCssScalaTags = Def setting ("com.github.japgolly.scalacss" %%% "ext-scalatags" % "0.5.1")

    object test {

      val scalaTest = Def setting ("org.scalatest" %%% "scalatest" % "3.0.1" % "test")
      val scalaCheck = Def setting ("org.scalacheck" %%% "scalacheck" % "1.13.4" % "test")
      val scalazScalaCheck = Def setting ("org.scalaz" %%% "scalaz-scalacheck-binding" % "7.2.8" % "test")

    }

  }

  object jvm {

    val slf4jSimple = "org.slf4j" % "slf4j-simple" % "1.7.22"
    val webjarsLocator = "org.webjars" % "webjars-locator" % "0.32"

    val scalazConcurrent = "org.scalaz" %% "scalaz-concurrent" % "7.2.8"

    val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.4.16"
    val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % "2.4.16"
    val akkaClusterMetrics = "com.typesafe.akka" %% "akka-cluster-metrics" % "2.4.16"
    val akkaClusterSharding = "com.typesafe.akka" %% "akka-cluster-sharding" % "2.4.16"
    val akkaClusterTools = "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.16"
    val akkaDistributedData = "com.typesafe.akka" %% "akka-distributed-data-experimental" % "2.4.16"
    val akkaPersistence = "com.typesafe.akka" %% "akka-persistence" % "2.4.16"
    val akkaPersistenceQuery = "com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.16"
    val akkaProtobuf = "com.typesafe.akka" %% "akka-protobuf" % "2.4.16"
    val akkaRemote = "com.typesafe.akka" %% "akka-remote" % "2.4.16"
    val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % "2.4.16"
    val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.4.16"
    val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.1"
    val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.1"
    val akkaHttpXml = "com.typesafe.akka" %% "akka-http-xml" % "10.0.1"

    val upickleAkkaHttp = "de.heikoseeberger" %% "akka-http-upickle" % "1.11.0"

    object webjars {

      val jquery = "org.webjars" % "jquery" % "3.1.1-1"

    }

    object provided {

      val scalajsStubs = "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided"

    }

    object test {

      val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % "2.4.16" % "test"
      val akkaMultiNodeTestKit = "com.typesafe.akka" %% "akka-multi-node-testkit" % "2.4.16" % "test"
      val akkaStreamTestKit = "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.16" % "test"
      val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % "10.0.1" % "test"

    }

  }

  object js {

    val dom = "org.scala-js" %%%! "scalajs-dom" % "0.9.1"
    val jquery = "be.doeraene" %%%! "scalajs-jquery" % "0.9.1"

  }


  object jsLibraries {

    val dom = RuntimeDOM
    val jquery = jvm.webjars.jquery / "jquery.js"

  }

}
