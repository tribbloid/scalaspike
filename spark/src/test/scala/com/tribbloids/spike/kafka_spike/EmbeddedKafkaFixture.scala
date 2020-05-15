package com.tribbloids.spike.kafka_spike

import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.scalatest.{BeforeAndAfterAll, Suite}

trait EmbeddedKafkaFixture extends Suite with BeforeAndAfterAll {

  val topicName = "test"
  val userDefinedConfig: EmbeddedKafkaConfig = {

    val brokerProperties = Map("message.max.bytes" -> "1000000000")
    EmbeddedKafkaConfig(kafkaPort = 9093, zooKeeperPort = 2181, brokerProperties)
  }

  override def beforeAll(): Unit = {
    super.beforeAll()

    EmbeddedKafka.start()(userDefinedConfig)
    EmbeddedKafka.createCustomTopic(topicName, partitions = 1, replicationFactor = 1)(userDefinedConfig)
  }

  override def afterAll(): Unit = {

    EmbeddedKafka.stop()
    Thread.sleep(2000)

    super.afterAll()
  }
}
