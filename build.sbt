ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / organization := "li.flxkbr"
ThisBuild / scalaVersion := "3.1.3"

lazy val dependencies: Seq[ModuleID] = Seq(
  "com.github.tototoshi" %% "scala-csv" % "1.3.10",
  "com.github.scopt"     %% "scopt"     % "4.1.0",
)

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
