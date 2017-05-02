package com.wouzar

import java.io.PrintWriter

import akka.http.javadsl.server.UnsupportedRequestContentTypeRejection
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.MalformedQueryParamRejection
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import com.wouzar.repository.FactorsRepository
import org.scalatest._

import scala.xml.NodeSeq


/**
  * Created by wouzar on 01.05.17.
  */
class WebServerSpec extends FlatSpec
  with Matchers
  with ScalatestRouteTest
  with Routes
  with ScalaXmlSupport {

  def prepareFiles(fp1: String, fp2: String): Unit = {

    val (cont1, cont2) = ("1.7,5.5,10", "1.5,63.4,66.3")
    val pw1 = new PrintWriter(fp1)
    pw1.println(cont1)
    pw1.close()
    val pw2 = new PrintWriter(fp2)
    pw2.println(cont2)
    pw2.close()

  }

  implicit val INPUT_FILE_PATH = "f1.csv"
  implicit val RESULT_FILE_PATH = "f2.csv"

  def withFiles(test: () => Unit): Unit = {
    prepareFiles(INPUT_FILE_PATH, RESULT_FILE_PATH)
    test()
  }

  val repository = new FactorsRepository(INPUT_FILE_PATH, RESULT_FILE_PATH)
  val calculator = new Calculator(repository)

  // GET route tests

  def get(parameter: String): HttpRequest = Get(s"/rest/calc?v1=$parameter")

  withFiles(() => "WebService" should "return proper value from result file in xml format" in {
    get("2") ~> routes ~> check {
      status shouldBe OK
      responseAs[NodeSeq].map(_.text.trim) shouldBe List("56.3")
    }
  })

  withFiles(() => "WebService" should "return bad request for negative parameter" in {
    get("-2") ~> routes ~> check {
      status shouldBe BadRequest
    }
  })

  withFiles(() => "WebService" should "reject request for double parameter" in {
    get("2.0") ~> routes ~> check {
      rejection shouldBe a[MalformedQueryParamRejection]
    }
  })

  withFiles(() => "WebService" should "reject request for string parameter" in {
    get("2f") ~> routes ~> check {
      rejection shouldBe a[MalformedQueryParamRejection]
    }
  })

  withFiles(() => "WebService" should "return bad request for index parameter bigger than factors amount" in {
    get("3") ~> routes ~> check {
      status shouldBe BadRequest
    }
  })

  // POST route tests

  def postWithEntity(entity: RequestEntity) = HttpRequest(
    HttpMethods.POST,
    uri = "/rest/calc",
    entity = entity
  )

  def postWithXml(xml: String): HttpRequest = postWithEntity(
    HttpEntity(
      MediaTypes.`text/xml`.toContentType(HttpCharsets.`UTF-8`), ByteString(xml)
    )
  )

  withFiles(() => "WebService" should "return some value" in {
    postWithXml(
      <data>
        <v2>4</v2>
        <v3>2</v3>
        <v4>1</v4>
      </data>.toString
    ) ~> routes ~> check {
      val fileResult = scala.io.Source.fromFile(RESULT_FILE_PATH).getLines().toSeq.mkString("")
      responseAs[NodeSeq].map(_.text.trim) shouldBe List("1")
      fileResult shouldBe "1.5,14.0,66.3"
    }
  })

  withFiles(() => "WebService" should "reject different content type than xml like json" in {
    postWithEntity(HttpEntity(MediaTypes.`application/json`, ByteString("{data: 4}"))) ~> routes ~> check {
      rejection shouldBe a[UnsupportedRequestContentTypeRejection]
    }
  })

  withFiles(() => "WebService" should "return bad request for unspecified parameters" in {
    postWithXml(
      <data></data>.toString
    ) ~> routes ~> check {
      status shouldBe BadRequest
    }
  })

  withFiles(() => "WebService" should "return bad request for at least one unspecified parameter" in {
    postWithXml(
      <data>
        <v2>4</v2>
        <v3>0</v3>
      </data>.toString
    ) ~> routes ~> check {
      status shouldBe BadRequest
    }
  })

  withFiles(() => "WebService" should "return bad request for malformed parameter" in {
    postWithXml(
      <data>
        <v2>4</v2>
        <v3>0</v3>
        <v4>5.5</v4>
      </data>.toString
    ) ~> routes ~> check {
      status shouldBe BadRequest
    }
  })

  withFiles(() => "WebService" should "return bad request for index out of bound" in {
    postWithXml(
      <data>
        <v2>4</v2>
        <v3>3</v3>
        <v4>3</v4>
      </data>.toString
    ) ~> routes ~> check {
      status shouldBe BadRequest
    }
  })

}
