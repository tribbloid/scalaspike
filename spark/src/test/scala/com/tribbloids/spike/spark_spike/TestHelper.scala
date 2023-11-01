package com.tribbloids.spike.spark_spike

import org.apache.spark.serializer.{KryoSerializer, Serializer}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext, SparkEnv, SparkException}

import java.util.Properties
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

class TestHelper() {

  val properties = new Properties()

  val S3Path = Option(properties.getProperty("S3Path"))

  val AWSAccessKeyId = Option(properties.getProperty("AWSAccessKeyId"))
  val AWSSecretKey = Option(properties.getProperty("AWSSecretKey"))

  def SPARK_HOME: String = System.getenv("SPARK_HOME")

  final val MAX_TOTAL_MEMORY = 16 * 1024
  final val MEMORY_PER_CORE = 1024
  //  final val EXECUTOR_JVM_MEMORY_OVERHEAD = 256 //TODO: remove, too complex

  final val MAX_CORES = MAX_TOTAL_MEMORY / MEMORY_PER_CORE

  val METASTORE_PATH = "metastore_db"
  val WAREHOUSE_PATH = "warehouse"

  var sparkSessionInitialised: Boolean = false

  {
    Try {
      properties.load(ClassLoader.getSystemResourceAsStream(".rootkey.csv"))
    }.recoverWith {
      case _: Throwable =>
        Try {
          properties.load(ClassLoader.getSystemResourceAsStream("rootkey.csv"))
        }
    }.getOrElse {
      println("rootkey.csv is missing")
    }

    if (S3Path.isDefined)
      println("Test on AWS S3 with credentials provided by rootkey.csv")

    AWSAccessKeyId.foreach {
      System.setProperty("fs.s3.awsAccessKeyId", _) // TODO: useless here? set in conf directly?
    }
    AWSSecretKey.foreach {
      System.setProperty("fs.s3.awsSecretAccessKey", _)
    }

  }

  lazy val numCores: Int = {
    val cap = Option(properties.getProperty("MaxCores"))
      .map(_.toInt)
      .getOrElse(MAX_CORES)
    var n = Math.min(
      Runtime.getRuntime.availableProcessors(),
      cap
    )

    if (n < 2) n = 2
    n
  }

  lazy val clusterSize_numCoresPerWorker_Opt: Option[(Int, Int)] = {
    Option(SPARK_HOME).flatMap { _ =>
      val tuple = (
        Option(properties.getProperty("ClusterSize")).map(_.toInt),
        Option(properties.getProperty("NumCoresPerWorker")).map(_.toInt)
      )
      tuple match {
        case (None, None) =>
          None
        case (Some(v1), Some(v2)) =>
          Some(v1 -> v2)
        case (Some(v1), None) =>
          Some(v1 -> Math.max(numCores / v1, 1))
        case (None, Some(v2)) =>
          Some(Math.max(numCores / v2, 1) -> v2)
      }
    }
  }

  def clusterSizeOpt: Option[Int] = clusterSize_numCoresPerWorker_Opt.map(_._1)
  def numCoresPerWorkerOpt: Option[Int] =
    clusterSize_numCoresPerWorker_Opt.map(_._2)

  def maxFailures: Int = {
    Option(properties.getProperty("MaxFailures")).map(_.toInt).getOrElse(4)
  }

  def numWorkers: Int = clusterSizeOpt.getOrElse(1)
  def numComputers: Int = clusterSizeOpt.map(_ + 1).getOrElse(1)

  def executorMemoryCapOpt: Option[Int] =
    clusterSizeOpt.map(clusterSize => MAX_TOTAL_MEMORY / clusterSize)

  def executorMemoryOpt: Option[Int] =
    for (n <- numCoresPerWorkerOpt; cap <- executorMemoryCapOpt) yield {
      Math.min(n * MEMORY_PER_CORE, cap)
    }

  lazy val serializerClass: Class[_ <: Serializer] = {
//    classOf[JavaSerializer]
    classOf[KryoSerializer]
  }

  /**
    * @return
    *   local mode: None -> local[n, 4] cluster simulation mode: Some(SPARK_HOME) -> local-cluster[m,n, mem]
    */
  lazy val coreSettings: Map[String, String] = {
    val masterEnv = System.getenv("SPARK_MASTER")

    val masterStr = if (masterEnv != null) {
      masterEnv
    } else if (clusterSizeOpt.isEmpty || numCoresPerWorkerOpt.isEmpty) {
      val masterStr = s"local[$numCores,$maxFailures]"
      println("initializing SparkContext in local mode:" + masterStr)
      masterStr
    } else {
      val masterStr =
        s"local-cluster[${clusterSizeOpt.get},${numCoresPerWorkerOpt.get},${executorMemoryOpt.get}]"
      println(s"initializing SparkContext in local-cluster simulation mode:" + masterStr)
      // TODO: Unstable! remove?
      masterStr
    }

    val base = if (masterStr.startsWith("local[")) {
      Map(
        "spark.master" -> masterStr
      )
    } else {
      Map(
        "spark.master" -> masterStr,
        "spark.home" -> SPARK_HOME,
        //        "spark.executor.memory" -> (executorMemoryOpt.get + "m"),
        "spark.driver.extraClassPath" -> sys.props("java.class.path"),
        "spark.executor.extraClassPath" -> sys.props("java.class.path"),
        "spark.task.maxFailures" -> maxFailures.toString
      )
    }

    base ++ Map(
      "spark.serializer" -> serializerClass.getName,
      //      .set("spark.kryo.registrator", "com.tribbloids.spookystuff.SpookyRegistrator")Incomplete for the moment
      "spark.kryoserializer.buffer.max" -> "512m",
      "spark.sql.warehouse.dir" -> WAREHOUSE_PATH,
      "hive.metastore.warehouse.dir" -> WAREHOUSE_PATH,
      "dummy.property" -> "dummy"
    )
  }

  // if SPARK_PATH & ClusterSize in rootkey.csv is detected, use local-cluster simulation mode
  // otherwise use local mode
  lazy val TestSparkConf: SparkConf = {

    // always use KryoSerializer, it is less stable than Native Serializer
    val conf: SparkConf = new SparkConf(false)

    conf.setAll(coreSettings)

    Option(System.getProperty("fs.s3.awsAccessKeyId")).foreach { v =>
      conf.set("spark.hadoop.fs.s3.awsAccessKeyId", v)
      conf.set("spark.hadoop.fs.s3n.awsAccessKeyId", v)
      conf.set("spark.hadoop.fs.s3a.awsAccessKeyId", v)
    }
    Option(System.getProperty("fs.s3.awsSecretAccessKey")).foreach { v =>
      conf.set("spark.hadoop.fs.s3.awsSecretAccessKey", v)
      conf.set("spark.hadoop.fs.s3n.awsSecretAccessKey", v)
      conf.set("spark.hadoop.fs.s3a.awsSecretAccessKey", v)
    }

    conf.setAppName("Test")
    conf
  }

  lazy val TestSparkSession = {

    val builder = SparkSession.builder
      .config(TestSparkConf)

    Try {
      builder.enableHiveSupport()
    }

    val session = builder.getOrCreate()
    val sc = session.sparkContext

    sparkSessionInitialised = true

    if (serializerClass == classOf[KryoSerializer]) ensureKryoSerializer(sc)
    session
  }

  lazy val TestSC = TestSparkSession.sparkContext
  lazy val TestSQL = TestSparkSession.sqlContext

  def setLoggerDuring[T](clazzes: Class[_]*)(fn: => T, level: String = "OFF"): T = {
    val logger_oldLevels = clazzes.map { clazz =>
      val logger = org.apache.log4j.Logger.getLogger(clazz)
      val oldLevel = logger.getLevel
      logger.setLevel(org.apache.log4j.Level.toLevel(level))
      logger -> oldLevel
    }
    try {
      fn
    } finally {
      logger_oldLevels.foreach {
        case (logger, oldLevel) =>
          logger.setLevel(oldLevel)
      }
    }
  }

  def ensureKryoSerializer(sc: SparkContext, rigorous: Boolean = false): Unit = {

    val ser = SparkEnv.get.serializer
    require(ser.isInstanceOf[KryoSerializer])

    // will print a long warning message into stderr, disabled by default
    if (rigorous) {
      val rdd = sc.parallelize(Seq(sc)).map { v =>
        v.startTime
      }

      try {
        rdd.reduce(_ + _)
        throw new AssertionError("should throw SparkException")
      } catch {
        case e: SparkException =>
          val ee = e
          Predef.assert(
            ee.getMessage.contains("com.esotericsoftware.kryo.KryoException"),
            "should be triggered by KryoException, but the message doesn't indicate that:\n" + ee.getMessage
          )
        case e: Throwable =>
          throw new AssertionError(s"Expecting SparkException, but ${e.getClass.getSimpleName} was thrown", e)
      }
    }
  }

  def intercept[EE <: Exception: ClassTag](fn: => Any) = {
    val trial = Try {
      fn
    }
    val expectedErrorName = implicitly[ClassTag[EE]].runtimeClass.getSimpleName
    trial match {
      case Failure(_: EE) =>
      case Failure(e) =>
        throw new AssertionError(s"Expecting $expectedErrorName, but get ${e.getClass.getSimpleName}", e)
      case Success(_) =>
        throw new AssertionError(s"expecting $expectedErrorName, but no exception was thrown")
    }
  }
}

object TestHelper extends TestHelper()
