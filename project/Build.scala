import sbt._
import Keys._


object Build extends Build {
  val moduleName = "rtp-domain-core-lib"

  lazy val root = Project(id = moduleName, base = file("."))
    .settings(
      name := moduleName,
      organization := "uk.gov.homeoffice",
      version := "1.0-SNAPSHOT",
      scalaVersion := "2.11.7",
      scalacOptions ++= Seq(
        "-feature",
        "-language:implicitConversions",
        "-language:higherKinds",
        "-language:existentials",
        "-language:reflectiveCalls",
        "-language:postfixOps",
        "-Yrangepos",
        "-Yrepl-sync"),
      // parallelExecution in Test := true,
      ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
      resolvers ++= Seq(
        "Artifactory Snapshot Realm" at "http://artifactory.registered-traveller.homeoffice.gov.uk/artifactory/libs-snapshot-local/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
        "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
        "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
        "Kamon Repository" at "http://repo.kamon.io"),
      libraryDependencies ++= Seq(
        "org.clapper" %% "grizzled-slf4j" % "1.0.2",
        "ch.qos.logback" % "logback-classic" % "1.1.3",
        "joda-time" % "joda-time" % "2.5" withSources(),
        "com.github.nscala-time" %% "nscala-time" % "2.0.0" withSources(),
        "org.joda" % "joda-convert" % "1.7" withSources(),
        "org.apache.poi" % "poi-ooxml" % "3.11-beta3" withSources(),
        "org.apache.commons" % "commons-lang3" % "3.3.2" withSources(),
        "org.mongodb" %% "casbah-core" % "2.8.1" withSources(),
        "com.novus" %% "salat" % "1.9.9",
        "com.github.scopt" %% "scopt" % "3.2.0" withSources(),
        "com.github.nscala-time" %% "nscala-time" % "2.0.0" withSources(),
        "org.json4s" %% "json4s-native" % "3.2.11" withSources(),
        "org.json4s" %% "json4s-jackson" % "3.2.11" withSources(),
        "org.json4s" %% "json4s-ext" % "3.2.11" withSources(),
        "org.json4s" %% "json4s-mongo" % "3.2.11" withSources(),
        "org.clapper" %% "classutil" % "1.0.5" withSources(),
        "org.scala-lang.modules" %% "scala-pickling" % "0.10.0" withSources()),
      libraryDependencies ++= Seq(
        "com.github.fakemongo" % "fongo" % "1.6.2" % Test withSources(),
        "org.specs2" %% "specs2-core" % "3.6.2" % Test withSources(),
        "org.specs2" %% "specs2-mock" % "3.6.2" % Test withSources(),
        "org.specs2" %% "specs2-matcher-extra" % "3.6.2" % Test withSources(),
        "org.mockito" % "mockito-all" % "1.10.19" % Test withSources(),
        "org.scalatest" %% "scalatest" % "2.2.4" % Test withSources())
    )


}