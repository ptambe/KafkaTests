package base

import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import kafka.utils.ZkUtils
import org.apache.commons.io.FileUtils
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
/**
  * Created by Prashant Tambe on 9/15/16.
  */
class BaseKafkaSpec extends FlatSpec with Matchers with BeforeAndAfterAll with LazyLogging{

  lazy val zkUtils = ZkUtils("localhost:2181", 30000,30000, false)

  lazy val producer = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("acks", "all")
    props.put("retries", "0")
    props.put("batch.size", "10") //Since we are testing this will be larger generally
    props.put("linger.ms", "1")
    props.put("buffer.memory", "1048576") //1MB
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    new KafkaProducer[String, String](props)
  }

  lazy val consumer = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("group.id", "test")
    props.put("enable.auto.commit", "true")
    props.put("auto.commit.interval.ms", "500");
    props.put("session.timeout.ms", "30000")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    new KafkaConsumer[String, String](props)
  }

  override def beforeAll(): Unit = {
    ZookeeperLocalServer.start
    KafkaLocalServer.start
    Thread.sleep(2000) //Let the Kafka server get settled
  }

  override def afterAll(): Unit = {
    consumer.close()
    producer.close()
    KafkaLocalServer.stop
    ZookeeperLocalServer.stop
    FileUtils.deleteDirectory(ZookeeperLocalServer.dataDir)
  }
}
