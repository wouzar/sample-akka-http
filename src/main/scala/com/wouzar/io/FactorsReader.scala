package com.wouzar.io

import scala.util.Try

/**
  * Created by wouzar on 30.04.17.
  */
trait FactorsReader {

  def readFactors(): Try[Seq[Factor]]

}

case class Factor(value: Double)
