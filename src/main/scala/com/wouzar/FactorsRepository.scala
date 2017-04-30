package com.wouzar

/**
  * Created by wouzar on 30.04.17.
  */
class FactorsRepository(final private val inputFilePath: String,
                        final private val resultFilePath: String) {

  def getFactors: Seq[Factor] = new FactorsCSVReader(inputFilePath).readFactors()

  def setFactors(index: Int, result: Factor): Unit = {
    val factors = new FactorsCSVReader(resultFilePath).readFactors()
    new FactorsCSVWriter(resultFilePath)
      .writeFactors(factors.patch(index, Seq(result), 1))
  }

}
