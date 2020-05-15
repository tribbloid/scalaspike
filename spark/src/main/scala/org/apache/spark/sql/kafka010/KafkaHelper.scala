package org.apache.spark.sql.kafka010

import org.apache.spark.sql.SQLContext

object KafkaHelper {

  def sink(
      sqlContext: SQLContext,
      executorKafkaParams: Map[String, Object],
      topic: Option[String]
  ): KafkaSink = {

    import scala.collection.JavaConverters._

    new KafkaSink(sqlContext, executorKafkaParams.asJava, topic)
  }
}
