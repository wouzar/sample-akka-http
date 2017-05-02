package com.wouzar

import java.io.{File, PrintWriter}
import java.util.UUID

import com.wouzar.io.Factor
import com.wouzar.repository.FactorsRepository
import org.scalatest.{FlatSpec, Matchers}
import FileHelper.{withCalculator, withCalculatorAndResultFile}

import scala.util.{Failure, Success}

/**
  * Created by wouzar on 02.05.17.
  */
class CalculatorSpec extends FlatSpec with Matchers {

  val (cont1, cont2) = ("1.7,5.5,10", "1.5,63.4,66.3")
  def _withCalculator: ((Calculator) => Any) => Unit =
    withCalculator(cont1, cont2)(_)
  def _withCalculatorAndResultFile: ((Calculator, String) => Any) => Unit =
    withCalculatorAndResultFile(cont1, cont2)(_)

  _withCalculator(calculator => "Calculator" should "return failure for negative and out of range values" in {
    calculator.correctFactor(-1) match {
      case Success(_) => fail()
      case Failure(e) => e.isInstanceOf[IndexOutOfBoundsException]
    }
    calculator.correctFactor(3) match {
      case Success(_) => fail()
      case Failure(e) => e.isInstanceOf[ArrayIndexOutOfBoundsException]
    }
  })

  _withCalculator(calculator => "Calculator" should "return meaningful value for correct parameter" in {
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

  _withCalculatorAndResultFile((calculator, resultFilePath) => "Calculator" should "return meaningful value for correct parameters" in {
    calculator.doCalculation(2, 1, 2) match {
      case Success(cond) =>
        cond shouldBe true
        FileHelper.getDataAsString(resultFilePath) shouldBe "1.5,63.4,7.5"
      case Failure(e) =>
        println(e.toString)
        fail()
    }
  })

}
