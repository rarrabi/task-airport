name := "scalajs-hello-world"
version in ThisBuild := "experimental-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.1"
scalacOptions in ThisBuild := Seq("-deprecation", "-feature", "-unchecked")

lazy val root =
  (project in file("."))
    .aggregate(
      scalajsHelloWorldJVM,
      scalajsHelloWorldJS,
      scalajsHelloWorldJSOutput
    )
    .settings(
      libraryDependencies := Seq.empty,
      publishArtifact := false
    )

lazy val scalajsHelloWorld =
  (crossProject in file("."))
    .settings(
      name := "scalajs-hello-world",
      libraryDependencies ++= Dependencies.shared.value
    )
    .jvmSettings(
      mainClass in Compile := Some("hello.world.Server"),
      libraryDependencies ++= Dependencies.jvm.value
    )
    .jsSettings(
      mainClass in Compile := Some("hello.world.Client"),
      persistLauncher in Compile := true,
      libraryDependencies ++= Dependencies.js.value,
      jsDependencies ++= Dependencies.jsDependencies.value
    )

lazy val scalajsHelloWorldJVM =
  scalajsHelloWorld.jvm
    .dependsOn(scalajsHelloWorldJSOutput)

lazy val scalajsHelloWorldJS =
  scalajsHelloWorld.js

lazy val scalajsHelloWorldJSOutput =
  (project in file(s"js/target"))
    .settings(
      name := s"${(name in scalajsHelloWorldJS).value}-js-output",
      sourceDirectories := Seq.empty,
      managedResources in Compile := Seq(
        (fastOptJS in(scalajsHelloWorldJS, Compile)).value.data,
        (fastOptJS in(scalajsHelloWorldJS, Compile)).value.metadata(scalaJSSourceMap),
        (packageScalaJSLauncher in(scalajsHelloWorldJS, Compile)).value.data
      ),
      managedResourceDirectories in Compile += (classDirectory in(scalajsHelloWorldJS, Compile)).value.getParentFile
    )
