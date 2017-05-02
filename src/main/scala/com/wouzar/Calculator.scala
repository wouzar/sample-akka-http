package com.wouzar

import com.wouzar.io.Factor
import com.wouzar.repository.FactorsRepository
import com.wouzar.repository.FactorsRepository.{InputFilePath, ResultFilePath}

import scala.util.Try

/**
  * Created by wouzar on 30.04.17.
  */
class Calculator(repository: FactorsRepository) {

  def correctFactor(v1: Int): Try[Factor] =
    for {
      f2 <- repository.readFactors(ResultFilePath)
      factor <- Try(f2(v1).value)
      value = if (factor > 10) {
        factor - 10
      } else factor
    } yield Factor(value)


  def doCalculation(v2: Int, v3: Int, v4: Int): Try[Boolean] =
    for {
      f1 <- repository.readFactors(InputFilePath)
      value <- Try(f1(v3).value)
      (newValue, condition) = if (value + v2 < 10) {
        (value + v2, true)
      } else (value + v2, false)
      _ <- repository.writeFactors(v4, Factor(newValue))
    } yield condition

}
