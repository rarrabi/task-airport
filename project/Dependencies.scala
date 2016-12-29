import sbt._

//noinspection TypeAnnotation
object Dependencies {

  val shared = Def setting Seq(
    Libraries.shared.cats.value,
    Libraries.shared.scalactic.value,
    Libraries.shared.scalaz.value,
    Libraries.shared.scalazEffect.value,
    Libraries.shared.shapeless.value,
    Libraries.shared.spire.value,

    Libraries.shared.autowire.value,
    Libraries.shared.upickle.value,

    Libraries.shared.scalaTags.value,
    Libraries.shared.scalaCss.value,
    Libraries.shared.scalaCssScalaTags.value,

    Libraries.shared.test.scalaTest.value,
    Libraries.shared.test.scalaCheck.value,
    Libraries.shared.test.scalazScalaCheck.value
  )

  val jvm = Def setting Seq(
    Libraries.jvm.slf4jSimple,
    Libraries.jvm.webjarsLocator,

    Libraries.jvm.scalazConcurrent,

    Libraries.jvm.akkaActor,
    Libraries.jvm.akkaCluster,
    Libraries.jvm.akkaClusterMetrics,
    Libraries.jvm.akkaClusterSharding,
    Libraries.jvm.akkaClusterTools,
    Libraries.jvm.akkaDistributedData,
    Libraries.jvm.akkaPersistence,
    Libraries.jvm.akkaPersistenceQuery,
    Libraries.jvm.akkaProtobuf,
    Libraries.jvm.akkaRemote,
    Libraries.jvm.akkaSlf4j,
    Libraries.jvm.akkaStream,
    Libraries.jvm.akkaHttp,
    Libraries.jvm.akkaHttpSprayJson,
    Libraries.jvm.akkaHttpXml,

    Libraries.jvm.upickleAkkaHttp,

    Libraries.jvm.webjars.jquery,

    Libraries.jvm.provided.scalajsStubs,

    Libraries.jvm.test.akkaTestKit,
    Libraries.jvm.test.akkaMultiNodeTestKit,
    Libraries.jvm.test.akkaStreamTestKit,
    Libraries.jvm.test.akkaHttpTestKit
  )

  val js = Def setting Seq(
    Libraries.js.javaLogging,

    Libraries.js.dom,
    Libraries.js.jquery
  )

  val jsDependencies = Def setting Seq(
    Libraries.jsLibraries.dom,
    Libraries.jsLibraries.jquery
  )

}
