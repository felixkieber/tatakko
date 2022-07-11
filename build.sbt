ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / organization := "li.flxkbr"
ThisBuild / scalaVersion := "3.1.3"

lazy val assemblySettings: Seq[SettingsDefinition] = Seq(
  assembly / assemblyJarName := "tatakko.jar",
  assembly / mainClass       := Some("Tatakko"),
)

lazy val testSettings: Seq[SettingsDefinition] = Seq(
  Test / logBuffered := false
)

lazy val root = (project in file("."))
  .settings(
    name := "tatakko",
    libraryDependencies ++= Dependencies.all,
    idePackagePrefix := Some("li.flxkbr"),
  )
  .settings(assemblySettings: _*)
  .settings(testSettings: _*)
