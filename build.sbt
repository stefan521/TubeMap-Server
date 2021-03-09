name := """tube-server"""
organization := "com.github.stefan521"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.stefan521.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.stefan521.binders._"
