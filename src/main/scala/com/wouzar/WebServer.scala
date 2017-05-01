package com.wouzar

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.wouzar.repository.FactorsRepository

import scala.concurrent.ExecutionContext
import scala.io.StdIn
import scala.util.Try
import scala.xml.NodeSeq

/**
  * Created by wouzar on 30.04.17.
  */

trait Routes extends ScalaXmlSupport {

  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  implicit val executionContext: ExecutionContext

  val calculator: Calculator

  def parseParameter(key: String, ns: NodeSeq): Try[Int] = {
    val newNs = ns \ key
    Try(newNs.text.toInt)
  }

  val route: Route =
    path("rest" / "calc") {
      get {
        parameter('v1.as[Int]) { v1 =>
          complete(
            <result>
              {calculator.correctFactor(v1).value}
            </result>
          )
        }
      } ~
        post {
          entity(as[NodeSeq]) { xml =>
            val result = for {
              v2 <- parseParameter("v2", xml)
              v3 <- parseParameter("v3", xml)
              v4 <- parseParameter("v4", xml)
            } yield {
              calculator.doCalculation(v2, v3, v4)
            }
            result.map { condition =>
              complete(
                <result>
                  {if (condition) 0 else 1}
                </result>)
            }.getOrElse(
              complete(HttpResponse(BadRequest, entity = "Wrong parameters!"))
            )
          }
        }
    }

}

object WebServer extends App with Routes {

  override implicit val system = ActorSystem("my-actor-system")
  override implicit val materializer = ActorMaterializer()
  override implicit val executionContext = system.dispatcher

  val repository = new FactorsRepository("f1.csv", "f2.csv")
  val calculator = new Calculator(repository)

  val (host, port) = ("localhost", 8080)
  val bindingFuture = Http().bindAndHandle(route, host, port)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())


}
