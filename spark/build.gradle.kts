val vs = versions()

repositories {
    maven("https://repos.spark-packages.org")
}

dependencies {

    api(project(":prover-commons:spark"))
    testFixturesApi(testFixtures(project(":prover-commons:spark")))

    implementation("org.apache.spark:spark-sql_${vs.scala.binaryV}:${vs.spark.v}")
    implementation("org.apache.spark:spark-mllib_${vs.scala.binaryV}:${vs.spark.v}")

    implementation("org.typelevel:frameless-dataset_${vs.scala.binaryV}:0.16.0")
//    implementation("graphframes:graphframes:0.8.3-spark${vs.spark.binaryV}-s_${vs.scala.binaryV}")

    testImplementation("org.apache.spark:spark-yarn_${vs.scala.binaryV}:${vs.spark.v}")

//    implementation("org.apache.spark:spark-sql-kafka-0-10_${vs.scala.binaryV}:${vs.spark.v}")
    implementation("io.github.embeddedkafka:embedded-kafka-streams_${vs.scala.binaryV}:3.6.1")

    implementation("uk.co.gresearch.spark:spark-extension_2.13:2.11.0-3.5")
}