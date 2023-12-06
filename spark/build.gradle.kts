val vs = versions()
val sparkBinaryV = "3.4"
val sparkV = "${sparkBinaryV}.1"

repositories {
    maven("https://repos.spark-packages.org")
}

dependencies {

    implementation(project(":common"))

    implementation("org.apache.spark:spark-sql_${vs.scala.binaryV}:${sparkV}")
    implementation("org.apache.spark:spark-mllib_${vs.scala.binaryV}:${sparkV}")

    implementation("org.typelevel:frameless-dataset_${vs.scala.binaryV}:0.15.0")
    implementation("graphframes:graphframes:0.8.3-spark${sparkBinaryV}-s_${vs.scala.binaryV}")

    testImplementation("org.apache.spark:spark-yarn_${vs.scala.binaryV}:${sparkV}")

    implementation("org.apache.spark:spark-sql-kafka-0-10_${vs.scala.binaryV}:${sparkV}")
    implementation("io.github.embeddedkafka:embedded-kafka-streams_${vs.scala.binaryV}:3.6.0")

    implementation("uk.co.gresearch.spark:spark-extension_2.13:2.10.0-3.5")
}