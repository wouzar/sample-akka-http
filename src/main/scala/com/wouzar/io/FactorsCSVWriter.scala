package com.wouzar.io

import java.io.PrintWriter

import scala.util.Try

/**
  * Created by wouzar on 30.04.17.
  */
class FactorsCSVWriter(val filename: String) extends FactorsWriter {

  override def writeFactors(factors: Seq[Factor]): Try[Unit] = {
    for {
      printWriter <- Try(new PrintWriter(filename))
    } yield {
      printWriter.println(factors.map(_.value).mkString(","))
      printWriter.close()

    }
  }

}
