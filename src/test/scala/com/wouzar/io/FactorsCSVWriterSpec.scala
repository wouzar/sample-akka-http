package com.wouzar.io

import com.wouzar.FileHelper
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by wouzar on 02.05.17.
  */
class FactorsCSVWriterSpec extends FlatSpec
  with Matchers {

  val filename = "testfile.tmp"
  val writer = new FactorsCSVWriter(filename)

  "Factors CSV writer" should "left file empty for empty sequence of factors" in {
    writer.writeFactors(Seq[Factor]())
    FileHelper.getDataAsString(filename) shouldBe ""
  }

  "Factors CSV writer" should "write factors to file separated by commas" in {
    writer.writeFactors(Seq[Factor](Factor(45.2), Factor(42)))
    FileHelper.getDataAsString(filename) shouldBe "45.2,42.0"
  }

}
