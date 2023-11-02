package com.tribbloids.spike.graphframes_spike

import com.tribbloids.spike.spark_spike.TestHelper
import org.apache.spark.sql.SparkSession
import org.scalatest.funspec.AnyFunSpec

class GraphDemo extends AnyFunSpec {

  val spark: SparkSession = TestHelper.TestSparkSession

  it("first") {
    // import graphframes package
    // Create a Vertex DataFrame with unique ID column "id"
    val v = spark
      .createDataFrame(
        List(
          ("a", "Alice", 34),
          ("b", "Bob", 36),
          ("c", "Charlie", 30)
        )
      )
      .toDF("id", "name", "age")
    // Create an Edge DataFrame with "src" and "dst" columns
    val e = spark
      .createDataFrame(
        List(
          ("a", "b", "friend"),
          ("b", "c", "follow"),
          ("c", "b", "follow")
        )
      )
      .toDF("src", "dst", "relationship")

    // Create a GraphFrame
    import org.graphframes.GraphFrame
    val g = GraphFrame(v, e)

    g.triplets.show()

    // Query: Get in-degree of each vertex.
    g.inDegrees.show()

    // Query: Count the number of "follow" connections in the graph.
    g.edges.filter("relationship = 'follow'").count()

    // Run PageRank algorithm, and show results.
    val results = g.pageRank.resetProbability(0.01).maxIter(20).run()
    results.vertices.select("id", "pagerank").show()
  }
}

object GraphDemo {

  case class Apartment(city: String, surface: Int, price: Double, bedrooms: Int)

}
