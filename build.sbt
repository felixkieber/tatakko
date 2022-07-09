ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / organization := "li.flxkbr"
ThisBuild / scalaVersion := "3.1.3"

val circeVersion = "0.14.2"

lazy val circeDeps: Seq[ModuleID] = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
).map(_ % circeVersion)

lazy val dependencies: Seq[ModuleID] = Seq(
  "com.github.tototoshi" %% "scala-csv" % "1.3.10",
  "com.github.scopt"     %% "scopt"     % "4.1.0",
) ++ circeDeps

lazy val assemblySettings: Seq[SettingsDefinition] = Seq(
  assembly / assemblyJarName := "tatakko.jar",
  assembly / mainClass       := Some("Tatakko"),
)

lazy val root = (project in file("."))
  .settings(
    name := "tatakko",
    libraryDependencies ++= dependencies,
  )
  .settings(assemblySettings: _*)
