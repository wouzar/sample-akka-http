package com.java.wouzar

import scala.util.Try

/**
  * Created by wouzar on 30.04.17.
  */
class FactorsCSVReader(val filename: String) extends FactorsReader {

  override def readFactors(): Seq[Factor] = {
    val result = for {
      line <- scala.io.Source
        .fromFile(filename)
        .getLines()
        .toSeq
      values = line
        .split(",")
        .map(_.trim)
        .flatMap(x => Try(Factor(x.toDouble)).toOption)
    } yield values
    result.flatten
  }

}
