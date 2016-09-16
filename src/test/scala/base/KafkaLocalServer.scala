package base

import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import kafka.server.{KafkaConfig, KafkaServerStartable}

/**
  * Created by Prashant Tambe on 9/15/16.
  */
object KafkaLocalServer extends LazyLogging {
  val props = new Properties()
  props.setProperty("zookeeper.connect", "localhost:2181")
  val server = new KafkaServerStartable(KafkaConfig(props))
  def start = {
    logger.info("Starting Kafka server ...")
    server.startup
    logger.info("Started Kafka server !!")
  }
  def stop = {
    logger.info("Stopping Kafka server ...")
    server.shutdown
    logger.info("Stopped Kafka server !!")
  }
}
