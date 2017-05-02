package com.wouzar

import java.io.{File, FileWriter}
import java.util.UUID

import com.wouzar.repository.FactorsRepository

/**
  * Created by wouzar on 02.05.17.
  */
object FileHelper {

  def getDataAsString(filename: String): String = {
    scala.io.Source.fromFile(filename).getLines().toSeq.mkString("")
  }

  def withFile(content: String)(testCode: String => Any): Unit = {
    val filename = UUID.randomUUID().toString
    val file = File.createTempFile(filename, "tmp")
    val writer = new FileWriter(file)
    try {
      writer.write(content)
      testCode(file.getAbsolutePath)
    }
    finally writer.close()
  }

  def withFiles(cont1: String, cont2: String = "")(testCode: (String, String) => Any): Unit = {
    withFile(cont1) { fstFileName =>
      withFile(cont2) { sndFileName =>
        testCode(fstFileName, sndFileName)
      }
    }
  }

  def withRepository(cont1: String, cont2: String = "")
                    (testCode: (FactorsRepository) => Any): Unit = {
    withFiles(cont1, cont2) { (fstFileName, sndFileName) =>
      val repository = new FactorsRepository(fstFileName, sndFileName)
      testCode(repository)
    }
  }

  def withRepositoryAndResultFile(cont1: String, cont2: String = "")
                                 (testCode: (FactorsRepository, String) => Any): Unit = {
    withFiles(cont1, cont2) { (fstFileName, sndFileName) =>
      val repository = new FactorsRepository(fstFileName, sndFileName)
      testCode(repository, sndFileName)
    }
  }

  def withCalculator(cont1: String, cont2: String = "")
                    (testCode: (Calculator) => Any): Unit = {
    withRepository(cont1, cont2) { repo =>
      val calculator = new Calculator(repo)
      testCode(calculator)
    }
  }

  def withCalculatorAndResultFile(cont1: String, cont2: String = "")
                                 (testCode: (Calculator, String) => Any): Unit = {
    withRepositoryAndResultFile(cont1, cont2) { (repo, sndFileName) =>
      val calculator = new Calculator(repo)
      testCode(calculator, sndFileName)
    }
  }

}
