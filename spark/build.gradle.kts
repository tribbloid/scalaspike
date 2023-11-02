val vs = versions()
val sparkBinaryV = "3.5"
val sparkV = "${sparkBinaryV}.0"

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
    implementation("io.github.embeddedkafka:embedded-kafka-streams_${vs.scala.binaryV}:3.5.1")
}