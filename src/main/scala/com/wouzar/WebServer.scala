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

  val calculator: Calculator

  def parseParameter(key: String, ns: NodeSeq): Try[Int] = {
    val newNs = ns \ key
    Try(newNs.text.toInt)
  }

  val routes: Route =
    path("rest" / "calc") {
      get {
        parameter('v1.as[Int]) { v1 =>
          complete(
            <result>{calculator.correctFactor(v1).value}</result>
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
                <result>{if (condition) 0 else 1}</result>
              )
            }.getOrElse(
              complete(HttpResponse(BadRequest, entity = "Wrong parameters!"))
            )
          }
        }
    }

}

class WebServer(implicit val system: ActorSystem,
                implicit val materializer: ActorMaterializer,
                implicit val executionContext: ExecutionContext) extends Routes {

  val repository = new FactorsRepository("f1.csv", "f2.csv")
  val calculator = new Calculator(repository)

  def start(host: String, port: Int): Unit = {
    val bindingFuture = Http().bindAndHandle(routes, host, port)

    println(s"Server online at http://$host:$port/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

}

object WebServer extends App {

  implicit val actorSystem = ActorSystem("my-actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher

  val server = new WebServer()
  server.start("localhost", 8080)

}
