package com.tribbloids.spike.spark_spike

import ai.acyclic.prover.commons.spark.TestHelper
import org.apache.spark.SparkContext
import org.apache.spark.util.LongAccumulator
import org.scalatest.funspec.AnyFunSpec

class RDDCachingVSLazyComputing extends AnyFunSpec {

  import RDDCachingVSLazyComputing._
  import org.apache.spark.storage.StorageLevel._

  val methods = Seq(
//    MEMORY_ONLY,
    MEMORY_ONLY_SER,
    MEMORY_AND_DISK_SER,
    DISK_ONLY
  )

  @transient val sc: SparkContext = TestHelper.TestSC

  methods.foreach { level =>
    describe(s"caching level $level") {

      it("can avoid double lazy execution by transformation") {

        val acc = new LongAccumulator()
        sc.register(acc)

        val proto = sc.parallelize(1 to 1000)

        val units = proto.map { v =>
          ComputingUnit(v)
        }

        units.persist(level)

        for (i <- 1 to 3) {

          units
            .map { v =>
              v.acc = acc
              v.rendered
            }
            .collect()

          assert(acc.value == 1000)
        }
      }

      it("can avoid double lazy execution by action") {

        val acc = new LongAccumulator()
        sc.register(acc)

        val proto = sc.parallelize(1 to 1000)

        val units = proto.map { v =>
          ComputingUnit(v)
        }

        units.persist(level)

        for (i <- 1 to 5) {

          units.foreach { v =>
            v.acc = acc
            v.rendered
          }

          assert(acc.value == 1000)
        }
      }
    }
  }
}

object RDDCachingVSLazyComputing {

  case class ComputingUnit(
      v: Long
  ) {

    var acc: LongAccumulator = _

    lazy val rendered: String = {
      Option(acc).foreach(_.add(1L))

      Thread.sleep(10)
      v.toString
    }
  }
}
