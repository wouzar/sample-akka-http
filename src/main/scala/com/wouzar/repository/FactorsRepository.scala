package com.wouzar.repository

import com.wouzar.repository.FactorsRepository.{FilePath, InputFilePath, ResultFilePath}
import com.wouzar.{Factor, FactorsCSVReader, FactorsCSVWriter}

/**
  * Created by wouzar on 30.04.17.
  */

object FactorsRepository {

  sealed trait FilePath

  case object InputFilePath extends FilePath

  case object ResultFilePath extends FilePath

}

class FactorsRepository(private val inputFilePath: String,
                        private val resultFilePath: String) {

  def getFactors(filePathType: FilePath): Seq[Factor] = {

    val filePath = filePathType match {
      case InputFilePath => inputFilePath
      case ResultFilePath => resultFilePath
    }

    new FactorsCSVReader(filePath).readFactors()
  }

  def setFactors(index: Int, result: Factor): Unit = {
    val factors = new FactorsCSVReader(resultFilePath).readFactors()
    new FactorsCSVWriter(resultFilePath)
      .writeFactors(factors.patch(index, Seq(result), 1))
  }

}
