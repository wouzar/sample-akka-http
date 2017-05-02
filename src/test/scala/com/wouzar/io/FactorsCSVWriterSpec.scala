package com.wouzar.io

import com.wouzar.io.{Factor, FactorsCSVWriter}
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by wouzar on 02.05.17.
  */
class FactorsCSVWriterSpec extends FlatSpec
  with Matchers {

  val filename = "testfile.tmp"
  val writer = new FactorsCSVWriter(filename)

  def getDataFromFile(filename: String): String = {
    scala.io.Source.fromFile(filename).getLines().toSeq.mkString("")
  }


  "Factors CSV writer" should "left file empty for empty sequence of factors" in {
    writer.writeFactors(Seq[Factor]())
    getDataFromFile(filename) shouldBe ""
  }

  "Factors CSV writer" should "write factors to file separated by commas" in {
    writer.writeFactors(Seq[Factor](Factor(45.2), Factor(42)))
    getDataFromFile(filename) shouldBe "45.2,42.0"
  }

}
