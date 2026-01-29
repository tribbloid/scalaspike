package com.tribbloids.spike.spark.chill

import ai.acyclic.prover.commons.testlib.BaseSpec
import com.twitter.chill.ClosureCleaner
import java.io._
import scala.language.reflectiveCalls

class ClosureCleanerExampleSpec extends BaseSpec {

  def serialize(o: AnyRef): Unit = {
    val baos = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(baos)
    oos.writeObject(o)
    oos.close()
  }

  describe("ClosureCleaner") {

    it("should clean a closure that captures a non-serializable object if the capture is unused") {

      // We use an anonymous class as 'Outer'.
      // We use an anonymous class as 'Outer'.
      val outer = new Serializable { self =>
        val ns = new NotSerializable

        // We use an explicit anonymous class for the closure to ensure it captures 'Outer'
        // (as $outer pointer) by default, even if we don't use it in 'apply'.
        val closure = new scala.runtime.AbstractFunction0[Int] with Serializable {
          def apply(): Int = 42
          def unused() = self.toString // Force $outer field generation
        }
      }

      val closure = outer.closure

      // 1. Verify that the closure is NOT serializable initially
      // because it captures 'outer', which has 'ns'.
      intercept[NotSerializableException] {
        serialize(closure)
      }

      // 2. Clean the closure
      // ClosureCleaner detects that 'outer' is captured but NOT used in 'apply'.
      // It nulls out the '$outer' field.
      ClosureCleaner(closure)

      // 3. Verify that the closure IS serializable after cleaning
      serialize(closure)

      assert(closure() == 42)
    }
  }
}

class NotSerializable {
  val x = 1
}
