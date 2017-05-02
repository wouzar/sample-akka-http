package com.wouzar.io

import java.io.{File, PrintWriter}

import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success}

/**
  * Created by wouzar on 02.05.17.
  */
class FactorsCSVReaderSpec extends FlatSpec
  with Matchers {

  def genReader(content: String): FactorsCSVReader = {
    val filename = "testfile.tmp"
    val pw = new PrintWriter(new File(filename))
    pw.println(content)
    pw.close()
    new FactorsCSVReader(filename)
  }


  "Factors CSV reader" should "return empty list for empty file" in {

    genReader("").readFactors() match {
      case Success(sequence) => sequence should have size 0
      case Failure(e) =>
        println(e.toString)
        fail()
    }

  }

  "Factors CSV reader" should "return meaningful value for correct data in file" in {

    genReader("11.5,44.6").readFactors() match {
      case Success(sequence) => sequence shouldBe Seq(Factor(11.5), Factor(44.6))
      case Failure(e) =>
        println(e.toString)
        fail()
    }

  }

  "Factors CSV reader" should "return failure for no file existed" in {

    new FactorsCSVReader("nofile.csv").readFactors() shouldBe a[Failure[Seq[Factor]]]

  }

}
