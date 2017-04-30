package com.java.wouzar

/**
  * Created by wouzar on 30.04.17.
  */
trait FactorsReader {

  def readFactors(): Seq[Factor]

}

case class Factor(value: Double)
