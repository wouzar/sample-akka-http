package com.wouzar.repository

import java.io.{File, PrintWriter}
import java.util.UUID

import com.wouzar.FileHelper
import com.wouzar.FileHelper.{withRepository, withRepositoryAndResultFile}
import com.wouzar.io.Factor
import com.wouzar.repository.FactorsRepository.InputFilePath
import org.scalatest.{FlatSpec, Matchers}

import scala.util.{Failure, Success}

/**
  * Created by wouzar on 02.05.17.
  */
class FactorsRepositorySpec extends FlatSpec
  with Matchers {

  withRepository("")(repo => "Factors repository" should "return empty list for empty file" in {
    repo.readFactors(InputFilePath) match {
      case Success(sequence) => sequence should have size 0
      case Failure(e) =>
        println(e.toString)
        fail()
    }

  })


  withRepository("11.5,44.6")(repo => "Factors repository" should "return meaningful value for correct data in file" in {
    repo.readFactors(InputFilePath) match {
      case Success(sequence) => sequence shouldBe Seq(Factor(11.5), Factor(44.6))
      case Failure(e) =>
        println(e.toString)
        fail()
    }

  })

  "Factors repository" should "return failure for no file existed" in {

    new FactorsRepository("noexist.txt", "noexist2.txt")
      .readFactors(InputFilePath) shouldBe a[Failure[Seq[Factor]]]

  }

  withRepository("", "3.0,4.5")(repo => "Factors repository" should "return failure for negative or out of bound index for factor being inserted" in {
    repo.writeFactors(-1, Factor(4.5)) match {
      case Success(_) => fail()
      case Failure(e) => e.isInstanceOf[IndexOutOfBoundsException]
    }
    repo.writeFactors(3, Factor(4.5)) match {
      case Success(_) => fail()
      case Failure(e) => e.isInstanceOf[ArrayIndexOutOfBoundsException]
    }
  })

  withRepositoryAndResultFile("3.0,4.5", "1.0,5.5")((repo, resultFilePath) =>
    "Factors repository" should "correctly rewrite value in result file" in {
      repo.writeFactors(2, Factor(4.5)) match {
        case Success(_) =>
          FileHelper.getDataAsString(resultFilePath) shouldBe "1.0,5.5,4.5"
        case Failure(_) =>
          fail()
      }
    })


}
