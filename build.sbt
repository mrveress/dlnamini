ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "dlnamini",
    idePackagePrefix := Some("me.veress.dlnamini"),
    assembly/assemblyJarName := "dlnamini.jar"
  )

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.2",
  "org.scala-lang.modules" %% "scala-xml" % "2.1.0",
  "org.apache.commons" % "commons-exec" % "1.3"
)