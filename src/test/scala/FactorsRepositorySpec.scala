import java.io.{File, PrintWriter}

import com.wouzar.io.{Factor, FactorsCSVReader}
import com.wouzar.repository.FactorsRepository
import com.wouzar.repository.FactorsRepository.InputFilePath
import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success}

/**
  * Created by wouzar on 02.05.17.
  */
class FactorsRepositorySpec extends FlatSpec
  with Matchers {

  val inputFilePath = "f1.csv"
  val resultFilePath = "f2.csv"

  def writeToFile(filePath: String, content: String): Unit = {
    val pw = new PrintWriter(new File(filePath))
    pw.println(content)
    pw.close()
  }

  val repo = new FactorsRepository(inputFilePath, resultFilePath)

  "Factors repository" should "return empty list for empty file" in {

    writeToFile(inputFilePath, "")
    repo.readFactors(InputFilePath) match {
      case Success(sequence) => sequence should have size 0
      case Failure(e) =>
        println(e.toString)
        fail()
    }

  }

  "Factors repository" should "return meaningful value for correct data in file" in {

    writeToFile(inputFilePath, "11.5,44.6")
    repo.readFactors(InputFilePath) match {
      case Success(sequence) => sequence shouldBe Seq(Factor(11.5), Factor(44.6))
      case Failure(e) =>
        println(e.toString)
        fail()
    }

  }

  "Factors repository" should "return failure for no file existed" in {

    new FactorsRepository("noexist.txt", "noexist2.txt")
      .readFactors(InputFilePath) shouldBe a[Failure[Seq[Factor]]]

  }

  "Factors repository" should "return failure for negative or out of bound index for factor being inserted" in {
    writeToFile(resultFilePath, "3.0,4.5")
    repo.writeFactors(-1, Factor(4.5)) match {
      case Success(_) => fail()
      case Failure(e) => e.isInstanceOf[IndexOutOfBoundsException]
    }
    repo.writeFactors(3, Factor(4.5)) match {
      case Success(_) => fail()
      case Failure(e) => e.isInstanceOf[ArrayIndexOutOfBoundsException]
    }
  }

  "Factors repository" should "correctly rewrite value in result file" in {
    writeToFile(inputFilePath, "3.0,4.5")
    writeToFile(resultFilePath, "1.0,5.5")
    repo.writeFactors(2, Factor(4.5)) match {
      case Success(_) => scala.io.Source.fromFile(resultFilePath)
        .getLines.toSeq.mkString("") shouldBe "1.0,5.5,4.5"
      case Failure(_) => fail()
    }
  }


}
