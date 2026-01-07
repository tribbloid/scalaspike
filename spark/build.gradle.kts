val vs = versions()

repositories {
    maven("https://repos.spark-packages.org")
    maven("https://repository.apache.org/snapshots")
}

dependencies {

    api(project(":prover-commons:spark"))
    testFixturesApi(testFixtures(project(":prover-commons:spark")))

    implementation("org.apache.spark:spark-sql_${vs.scala.binaryV}:${vs.spark.v}")
    implementation("org.apache.spark:spark-mllib_${vs.scala.binaryV}:${vs.spark.v}")

    implementation("org.typelevel:frameless-dataset_${vs.scala.binaryV}:0.16.0")

    // Apache Sedona for spatial data processing (Spark 4.0)
    implementation("org.apache.sedona:sedona-spark-4.0_${vs.scala.binaryV}:1.8.1")


//    implementation("graphframes:graphframes:0.8.3-spark${vs.spark.binaryV}-s_${vs.scala.binaryV}")

    testImplementation("org.apache.spark:spark-yarn_${vs.scala.binaryV}:${vs.spark.v}")

    // Parquet Avro for in-memory serialization
    testImplementation("org.apache.parquet:parquet-avro:1.14.4")
    testImplementation("org.apache.avro:avro:1.12.1")

//    implementation("org.apache.spark:spark-sql-kafka-0-10_${vs.scala.binaryV}:${vs.spark.v}")
    implementation("io.github.embeddedkafka:embedded-kafka-streams_${vs.scala.binaryV}:4.1.0")

//    implementation("uk.co.gresearch.spark:spark-extension_2.13:2.12.0-3.5")
}