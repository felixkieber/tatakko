import sbt._

object Dependencies {
  val circeVersion     = "0.14.2"
  val catsVersion = "2.8.0"
  val scalatestVersion = "3.2.12"

  private lazy val circeDeps: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
  ).map(_ % circeVersion)

  private lazy val catsDeps: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core",
  ).map(_ % catsVersion)

  // private lazy val kittens =
  //   "org.typelevel" %% "kittens" % "3.0.0-M4"

  lazy val main: Seq[ModuleID] = Seq(
    "com.github.tototoshi" %% "scala-csv" % "1.3.10",
    "com.github.scopt"     %% "scopt"     % "4.1.0",
    "org.scalactic"        %% "scalactic" % scalatestVersion,
  ) ++ circeDeps ++ catsDeps //:+ kittens

  lazy val test: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % scalatestVersion
  ).map(_ % Test)

  lazy val all: Seq[ModuleID] = main ++ test
}
