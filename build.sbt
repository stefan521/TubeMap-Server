name := """tube-server"""
organization := "com.github.stefan521"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

val AkkaHttpVersion = "10.2.4"

libraryDependencies ++= Seq(
  // Defaults
  guice,
  clusterSharding,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,

  // Akka
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,

  // Joda
  "joda-time" % "joda-time" % "2.10.10",
  "com.typesafe.play" %% "play-json-joda" % "2.10.0-RC2",

  "org.mongodb" % "mongo-java-driver" % "3.12.8",

  "org.typelevel" %% "cats-core" % "2.1.1",
)


/*
  PlayKeys.devSettings are necessary because in dev mode the configuration is ignored:
  https://stackoverflow.com/questions/44873117/why-does-play-2-6-close-a-websocket-after-85-seconds-when-it-is-idle-while-play
*/
PlayKeys.devSettings := Seq(
  "play.server.http.idleTimeout" -> "3 minutes",
)
