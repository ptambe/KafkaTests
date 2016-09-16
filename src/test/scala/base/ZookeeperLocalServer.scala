package base

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import org.apache.curator.test.TestingServer

/**
  * Created by Prashant Tambe on 9/15/16.
  *
  * Using the test class from curator framework, since stopping the zookeeper server
  * is quite involved and curator-test seems to implement it nicely.
  *
  */
object ZookeeperLocalServer extends LazyLogging {
  val dataDir = new File("target/zkData/")
  val server = new TestingServer(2181, dataDir, false)
  def start = {
    logger.info("Starting Zookeeper server ...")
    server.start
    logger.info("Started Zookeeper server !!")
  }
  def stop = {
    logger.info("Stopping Zookeeper server ...")
    server.stop
    logger.info("Stopped Zookeeper server !!")
  }

}
