package base

import java.util.Properties

import kafka.admin.AdminUtils
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.ProducerRecord
import org.scalatest._

import scala.collection.JavaConverters._
/**
  * Created by Prashant Tambe on 9/15/16.
  */
class SampleTest extends BaseKafkaSpec with BeforeAndAfter {

  val topic = "mytopic"

  before {
    logger.info("Creating a topic")
    if( AdminUtils.topicExists(zkUtils, topic) )
      AdminUtils.deleteTopic(zkUtils, topic)
    AdminUtils.createTopic(zkUtils, topic, 1, 1, new Properties())
  }

  after {
    logger.info("Deleting topic")
    AdminUtils.deleteTopic(zkUtils, topic)
  }

  def retryTillTryCountOrSuccess[T](tries:Int)(s: T => Boolean, f: => T): Option[T] = {
    tries match {
      case 0 => None
      case iTr => {
        val t = f
        s(t) match {
          case false => {
            logger.info("retrying ... (" + iTr + ")")
            retryTillTryCountOrSuccess(iTr-1)(s, f)
          }
          case true => Some(t)
        }
      }
    }
  }

  "Local Kafka server" should "should allow sending and receiving messages " in {

    logger.info("Sending message")
    producer.send(new ProducerRecord[String, String](topic, "mykey1", "myvalue1"))
    producer.send(new ProducerRecord[String, String](topic, "mykey2", "myvalue2"))
    producer.send(new ProducerRecord[String, String](topic, "mykey3", "myvalue3"))
    producer.flush()

    consumer.subscribe(List(topic).asJava)
    consumer.seekToBeginning(List().asJava)

    val validate = { (cr: ConsumerRecords[String, String]) => !cr.isEmpty }
    val recs = retryTillTryCountOrSuccess[ConsumerRecords[String, String]](10)(validate, { consumer.poll(1000) })
    recs match {
      case Some(recs) => {
        recs.asScala.foreach( r => { logger.info("Got - " + r.key + " " + r.value) })
        succeed
      }
      case None => {
        logger.info("Messages not found")
        fail
      }
    }
  }

}
