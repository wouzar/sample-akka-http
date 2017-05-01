package com.wouzar

import java.io.PrintWriter

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.wouzar.repository.FactorsRepository
import org.scalatest._

import scala.xml.NodeSeq


/**
  * Created by wouzar on 01.05.17.
  */
class WebServerSpec extends FlatSpec with Matchers with ScalatestRouteTest with Routes {

  def prepareFiles(fp1: String, fp2: String): Unit = {
    val pw1 = new PrintWriter(fp1)
    pw1.println("1,28,64,53,13,54,63")
    pw1.close()
    val pw2 = new PrintWriter(fp2)
    pw2.println("42.0,29.0,452.2,24.0,4.3,4.0,1.0")
    pw2.close()

  }

  val INPUT_FILE_PATH = "f1.csv"
  val RESULT_FILE_PATH = "f2.csv"

  prepareFiles(INPUT_FILE_PATH, RESULT_FILE_PATH)

  val repository = new FactorsRepository(INPUT_FILE_PATH, RESULT_FILE_PATH)
  val calculator = new Calculator(repository)

  "WebService" should "return 4.0 value in xml format" in {
    Get(s"/rest/calc?v1=5") ~> routes ~> check {
      status shouldBe OK
      responseAs[NodeSeq] shouldBe <result>4.0</result>
    }
  }


}
