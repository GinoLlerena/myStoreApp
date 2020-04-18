import sbt.Keys.libraryDependencies

name := """myScalaProject"""
organization := "learningbydoing"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "com.typesafe.play" %% "play-slick" % "4.0.0"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0"
libraryDependencies += "com.github.stephenc.eaio-uuid" % "uuid" % "3.2.0"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.8.1"
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"
//we are going to need to replace this with the mysql connector
//libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"
libraryDependencies += "com.pauldijou" %% "jwt-core" % "2.1.0"
libraryDependencies += "com.sendgrid" % "sendgrid-java" % "4.3.0"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test



