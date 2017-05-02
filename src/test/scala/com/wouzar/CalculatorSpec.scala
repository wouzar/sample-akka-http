package com.wouzar

import java.io.{File, PrintWriter}

import com.wouzar.io.Factor
import com.wouzar.repository.FactorsRepository
import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success}

/**
  * Created by wouzar on 02.05.17.
  */
class CalculatorSpec extends FlatSpec with Matchers {

  def prepareFiles(fp1: String, fp2: String): Unit = {

    val (cont1, cont2) = ("1.7,5.5,10", "1.5,63.4,66.3")
    val pw1 = new PrintWriter(fp1)
    pw1.println(cont1)
    pw1.close()
    val pw2 = new PrintWriter(fp2)
    pw2.println(cont2)
    pw2.close()

  }

  def writeToFile(filePath: String, content: String): Unit = {
    val pw = new PrintWriter(new File(filePath))
    pw.println(content)
    pw.close()
  }

  implicit val INPUT_FILE_PATH = "f1.csv"
  implicit val RESULT_FILE_PATH = "f2.csv"

  def withFiles(test: () => Unit): Unit = {
    prepareFiles(INPUT_FILE_PATH, RESULT_FILE_PATH)
    test()
  }

  val calculator = new Calculator(new FactorsRepository(INPUT_FILE_PATH, RESULT_FILE_PATH))

  withFiles(() => "Calculator" should "return failure for negative and out of range values" in {
    calculator.correctFactor(-1) match {
      case Success(_) => fail()
      case Failure(e) => e.isInstanceOf[IndexOutOfBoundsException]
    }
    calculator.correctFactor(3) match {
      case Success(_) => fail()
      case Failure(e) => e.isInstanceOf[ArrayIndexOutOfBoundsException]
    }
  })

  withFiles(() => "Calculator" should "return meaningful value for correct parameter" in {
    calculator.correctFactor(2) match {
      case Success(factor) => factor shouldBe Factor(56.3)
      case Failure(e) =>
        println(e.toString)
        fail()
    }
    calculator.correctFactor(0) match {
      case Success(factor) => factor shouldBe Factor(1.5)
      case Failure(e) =>
        println(e.toString)
        fail()
    }
  })

  withFiles(() => "Calculator" should "return meaningful value for correct parameters" in {
    calculator.doCalculation(2,1,2) match {
      case Success(cond) =>
        cond shouldBe true
        scala.io.Source.fromFile(RESULT_FILE_PATH)
          .getLines().toSeq.mkString("") shouldBe "1.5,63.4,7.5"
      case Failure(e) =>
        println(e.toString)
        fail()
    }
  })

}
