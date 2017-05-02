package com.wouzar.io

import scala.util.Try

/**
  * Created by wouzar on 30.04.17.
  */
class FactorsCSVReader(val filename: String) extends FactorsReader {

  override def readFactors(): Try[Seq[Factor]] =
    for {
      line <- Try(scala.io.Source
        .fromFile(filename)
        .getLines()
        .toSeq)
      values <- Try(line.flatMap(_.split(",").map(_.trim)).map(x => Factor(x.toDouble)))
    } yield values

}
