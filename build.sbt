ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "dlnamini",
    idePackagePrefix := Some("me.veress.dlnamini")
  )

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.2"
)