name := "json-transformer"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.alibaba" % "fastjson" % "1.2.47",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.2.1",
  "com.typesafe" % "config" % "1.3.3",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.11.0",
  "org.apache.logging.log4j" % "log4j-api" % "2.11.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.11.0"
)