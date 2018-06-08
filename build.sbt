name := """UserService"""
organization := "fr.testProject"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.8.9" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.13" % Test
