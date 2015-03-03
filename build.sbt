name := """linkscraper"""

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-M4",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
