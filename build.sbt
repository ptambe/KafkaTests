name := "KafkaTests"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "0.10.0.1",
  "org.apache.kafka" % "kafka-clients" % "0.10.0.1",
  "org.apache.zookeeper" % "zookeeper" % "3.4.6",
  "org.apache.curator" % "curator-test" % "3.1.0" % "test",
  "org.scalactic" %% "scalactic" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "commons-io" % "commons-io" % "2.4"
)
