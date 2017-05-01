name := "sample-akka-http"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= {
  val scalaTestV = "3.0.3"
  val akkaHttpV = "10.0.5"
  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-xml" % akkaHttpV,
    "org.scalatest" %% "scalatest" % scalaTestV % "test"
  )
}
