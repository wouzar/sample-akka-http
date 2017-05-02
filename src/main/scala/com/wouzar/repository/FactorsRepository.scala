package com.wouzar.repository

import com.wouzar.io.{Factor, FactorsCSVReader, FactorsCSVWriter}
import com.wouzar.repository.FactorsRepository.{FilePath, InputFilePath, ResultFilePath}

import scala.util.Try

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

  def readFactors(filePathType: FilePath): Try[Seq[Factor]] = {

    val filePath = filePathType match {
      case InputFilePath => inputFilePath
      case ResultFilePath => resultFilePath
    }

    new FactorsCSVReader(filePath).readFactors()
  }

  def writeFactors(index: Int, result: Factor): Try[Unit] =
    for {
      factors <- new FactorsCSVReader(resultFilePath).readFactors()
      done <-
      new FactorsCSVWriter(resultFilePath)
        .writeFactors(factors.patch(index, Seq(result), 1))
    } yield done


}
