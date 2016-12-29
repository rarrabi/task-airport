name := "task-airport"
version in ThisBuild := "experimental-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.1"
scalacOptions in ThisBuild := Seq("-deprecation", "-feature", "-unchecked")

lazy val root =
  (project in file("."))
    .aggregate(
      taskAirportJVM,
      taskAirportJS,
      taskAirportResources
    )
    .settings(
      libraryDependencies := Seq.empty,
      publishArtifact := false
    )

lazy val taskAirport =
  (crossProject in file("."))
    .settings(
      name := "task-airport",
      libraryDependencies ++= Dependencies.shared.value
    )
    .jvmSettings(
      mainClass in Compile := Some("task.airport.Server"),
      libraryDependencies ++= Dependencies.jvm.value
    )
    .jsSettings(
      mainClass in Compile := Some("task.airport.Client"),
      persistLauncher in Compile := true,
      libraryDependencies ++= Dependencies.js.value,
      jsDependencies ++= Dependencies.jsDependencies.value
    )

lazy val taskAirportJVM =
  taskAirport.jvm
    .dependsOn(taskAirportResources)

lazy val taskAirportJS =
  taskAirport.js

lazy val taskAirportResources =
  (project in file(s"js/target"))
    .settings(
      name := s"${(name in taskAirportJS).value}-resources",
      sourceDirectories := Seq.empty,
      resourceDirectories := Seq.empty,
      managedResources in Compile := Seq(
        (fastOptJS in(taskAirportJS, Compile)).value.data,
        (fastOptJS in(taskAirportJS, Compile)).value.metadata(scalaJSSourceMap),
        (packageScalaJSLauncher in(taskAirportJS, Compile)).value.data
      ),
      managedResourceDirectories in Compile += (classDirectory in(taskAirportJS, Compile)).value.getParentFile
    )
