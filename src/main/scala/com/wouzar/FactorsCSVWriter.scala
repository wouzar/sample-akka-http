package com.wouzar

import java.io.PrintWriter

/**
  * Created by wouzar on 30.04.17.
  */
class FactorsCSVWriter(val filename: String) extends FactorsWriter {

  override def writeFactors(factors: Seq[Factor]): Unit = {
    val printWriter = new PrintWriter(filename)
    printWriter.println(factors.map(_.value).mkString(","))
    printWriter.close()
  }

}
