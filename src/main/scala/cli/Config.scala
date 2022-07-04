package cli

import scopt.OParser

case class Config(userCount: Int, questionCount: Int, questionViewCount: Int)

object Config {
  def parser: OParser[Unit, Config] = {
    val builder = OParser.builder[Config]
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
        help('h', "help")
          .text("prints this usage text"),
      )
    }
  }
}
