package conf

import scopt.OParser

import java.io.File
import java.nio.file.{FileSystems, Files, Paths}

case class CommandLineOptions(userCount: Int, questionCount: Int, questionViewCount: Int, destinationFolder: File)

object CommandLineOptions {

  val defaults: CommandLineOptions = CommandLineOptions(
    userCount = 50,
    questionCount = 400,
    questionViewCount = 50_000,
    destinationFolder = Paths.get(".").toFile,
  )

  def parser: OParser[Unit, CommandLineOptions] = {
    val builder = OParser.builder[CommandLineOptions]
    {
      import builder._
      OParser.sequence(
        programName("tatakko.jar"),
        head("tatakko", "0.1.0"),
        opt[Int]('u', "users")
          .action((u, conf) => conf.copy(userCount = u))
          .text("number of users to generate. default 50")
          .validate(u => if (u > 0) success else failure("<users> must be >0"))
          .valueName("<users>"),
        opt[Int]('q', "questions")
          .action((q, conf) => conf.copy(questionCount = q))
          .text("number of questions to generate. default 400")
          .validate(q => if (q > 0) success else failure("<questions> must be >0"))
          .valueName("<questions>"),
        opt[Int]('v', "views")
          .action((v, conf) => conf.copy(questionViewCount = v))
          .text("number of question views to generate. default 10000")
          .validate(v => if (v > 0) success else failure("<views> must be >0"))
          .valueName("<views>"),
        opt[File]('d', "dest")
          .action((dest, conf) => conf.copy(destinationFolder = dest))
          .text(s"destination folder where the files will be written. default .${FileSystems.getDefault.getSeparator}")
          .validate(f => if (f.isDirectory) success else failure("<dest> must be a valid directory"))
          .valueName("<dest>"),
        help('h', "help")
          .text("prints this usage text"),
      )
    }
  }
}
