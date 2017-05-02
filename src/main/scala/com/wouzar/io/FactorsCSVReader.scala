package com.wouzar.io

import scala.util.Try

/**
  * Created by wouzar on 30.04.17.
  */
class FactorsCSVReader(val filename: String) extends FactorsReader {

  override def readFactors(): Try[Seq[Factor]] =
    for {
      lines <- Try(scala.io.Source
        .fromFile(filename)
        .getLines()
        .toSeq)
      values <- Try {
        val numbers = lines.flatMap(_.split(",").map(_.trim))
        numbers.filter(_ != "").map(x => Factor(x.toDouble))
      }
    } yield values

}
