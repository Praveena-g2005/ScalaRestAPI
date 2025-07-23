ThisBuild / scalaVersion := "2.13.13"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaRestAPI",

    libraryDependencies ++= Seq(
      // Akka HTTP
      "com.typesafe.akka" %% "akka-http" % "10.2.10",
      "com.typesafe.akka" %% "akka-stream" % "2.6.20",

      // JSON4s for JSON serialization
      "org.json4s" %% "json4s-native" % "4.0.6",

      // Slick + PostgreSQL
      "com.typesafe.slick" %% "slick" % "3.4.1",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
      "org.postgresql" % "postgresql" % "42.2.27",

      // Logging
      "ch.qos.logback" % "logback-classic" % "1.4.11"
    )
  )
