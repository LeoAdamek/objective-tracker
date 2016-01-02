name := "objectiveTracker"

version := "1.0"

scalaVersion := "2.11.7"

javaOptions in run += s"-Djava.library.path=./lib"

libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-core" % "1.1.3",
  "com.typesafe.akka" %% "akka-actor" % "2.4.1"
)