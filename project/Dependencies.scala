import sbt._

object Dependencies {
  val circeVersion      = "0.14.2"
  val catsVersion       = "2.9.0"
  val catsEffectVersion = "3.4.5"
  val scalatestVersion  = "3.2.15"

  private lazy val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
  ).map(_ % circeVersion)

  private lazy val cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core"
  ).map(_ % catsVersion)

  private lazy val catsEffect: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-effect"
  ).map(_ % catsEffectVersion)

  // private lazy val kittens =
  //   "org.typelevel" %% "kittens" % "3.0.0-M4"

  lazy val main: Seq[ModuleID] = Seq(
    "com.github.tototoshi" %% "scala-csv" % "1.3.10",
    "com.github.scopt"     %% "scopt"     % "4.1.0",
    "org.scalactic"        %% "scalactic" % scalatestVersion,
  ) ++ circe ++ cats ++ catsEffect // :+ kittens

  lazy val test: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % scalatestVersion
  ).map(_ % Test)

  lazy val all: Seq[ModuleID] = main ++ test
}
