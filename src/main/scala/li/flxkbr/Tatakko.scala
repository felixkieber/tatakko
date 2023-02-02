package li.flxkbr

import cats.effect.{ExitCode, IO, IOApp}
import com.github.tototoshi.csv.*
import li.flxkbr.conf.CommandLineOptions
import scopt.OParser

import java.io.File
import scala.util.Try
import li.flxkbr.model.ModelPath

object Tatakko extends IOApp {

  def oldmain(args: Array[String]): Unit = {

    OParser.parse(CommandLineOptions.parser, args, CommandLineOptions.defaults) match {
      case Some(config) =>
      case None => System.exit(-1)
    }

  }

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      rawPath <- IO.pure("myfirst.path")
      safePath <- ModelPath.fromString[IO](rawPath)
    } yield ExitCode.Success
  }
}
