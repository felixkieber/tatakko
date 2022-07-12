package li.flxkbr

import com.github.tototoshi.csv.*
import li.flxkbr.conf.CommandLineOptions
import li.flxkbr.gen.*
import scopt.OParser

import java.io.File
import scala.util.Try

object Tatakko {

  def main(args: Array[String]): Unit = {

    OParser.parse(CommandLineOptions.parser, args, CommandLineOptions.defaults) match {
      case Some(config) =>
        val userGen = UserGenerator()

        val usersFile  = new File(s"${userGen.key}.csv")
        val userWriter = CSVWriter.open(usersFile)

        val users = Try {
          val users = userGen.bulk(config.userCount).toIndexedSeq
          userWriter.writeRow(userGen.headerRow)
          userWriter.writeAll(users.map(_.asRaw))
          users
        }

        userWriter.close()

        val questionGen = QuestionGenerator(users.get)

        val questionsFile = new File(s"${questionGen.key}.csv")
        val qWriter       = CSVWriter.open(questionsFile)

        val questions = Try {
          val questions = questionGen.bulk(config.questionCount).toIndexedSeq
          qWriter.writeRow(questionGen.headerRow)
          qWriter.writeAll(questions.map(_.asRaw))
          questions
        }

        qWriter.close()

        val sqvGen = SetupQuestionViewGenerator(users.get, questions.get)

        val sqvFile   = new File(s"${sqvGen.key}.csv")
        val sqvWriter = CSVWriter.open(sqvFile)

        val batchCount = Math.ceil(config.questionViewCount.toDouble / 1_000).toInt

        Try {
          sqvWriter.writeRow(sqvGen.headerRow)
          for _ <- 1 to batchCount do {
            for _ <- 1 to 1_000 do {
              sqvWriter.writeRow(sqvGen.nextRow)
            }
            sqvWriter.flush()
          }
        }

        sqvWriter.close()
      case None => System.exit(-1)
    }

  }
}
