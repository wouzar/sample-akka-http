package com.wouzar

import com.wouzar.io.Factor
import com.wouzar.repository.FactorsRepository
import com.wouzar.repository.FactorsRepository.{InputFilePath, ResultFilePath}

/**
  * Created by wouzar on 30.04.17.
  */
class Calculator(repository: FactorsRepository) {

  def correctFactor(v1: Int): Factor = {

    val f2 = repository.readFactors(ResultFilePath)
    // danger
    val factor = f2(v1).value

    val value = if (factor > 10) {
      factor - 10
    } else factor

    Factor(value)
  }

  def doCalculation(v2: Int, v3: Int, v4: Int): Boolean = {
    val f1 = repository.readFactors(InputFilePath)

    val (newValue, condition) = if (f1(v3).value + v2 < 10) {
      (f1(v3).value + v2, true)
    } else (f1(v3).value + v2, false)

    repository.writeFactors(v4, Factor(newValue))

    condition

  }

}
