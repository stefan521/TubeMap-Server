name := """tube-server"""
organization := "com.github.stefan521"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
//libraryDependencies += "com.typesafe.akka" %% "akka-cluster-tools" % AkkaVersion

/*
  PlayKeys.devSettings are necessary because in dev mode the configuration is ignored:
  https://stackoverflow.com/questions/44873117/why-does-play-2-6-close-a-websocket-after-85-seconds-when-it-is-idle-while-play
*/
PlayKeys.devSettings := Seq(
  "play.server.http.idleTimeout" -> "3 minutes",
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.stefan521.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.stefan521.binders._"
