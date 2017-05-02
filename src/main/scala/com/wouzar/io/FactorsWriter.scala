package com.wouzar.io

import scala.util.Try

/**
  * Created by wouzar on 30.04.17.
  */
trait FactorsWriter {

  def writeFactors(factors: Seq[Factor]): Try[Unit]

}
