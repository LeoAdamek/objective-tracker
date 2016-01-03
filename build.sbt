name := "objectiveTracker"

version := "1.0"

scalaVersion := "2.11.7"

javaOptions in run += s"-Djava.library.path=./lib"

lazy val akkaVersion = "2.4.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion
)