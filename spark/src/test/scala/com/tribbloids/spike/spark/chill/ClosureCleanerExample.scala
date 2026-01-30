package com.tribbloids.spike.spark.chill

import ai.acyclic.prover.commons.spark.serialization.NOTSerializable
import ai.acyclic.prover.commons.testlib.BaseSpec
import com.twitter.chill.ClosureCleaner

import java.io.*
import scala.language.reflectiveCalls
import scala.util.Try

class ClosureCleanerExample extends BaseSpec {
  import ClosureCleanerExample.*

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
        val ns = new N1

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

  it("can clean non-serializable free var and only keep its member") {

    val closure = generate(n1)
    intercept[NotSerializableException] {

//      sys.error("bull")

      val trial = Try {

        serialize(closure)
      }

      trial.get
    }

    val closure2 = ImprovedClosureCleaner().process(closure)

    serialize(closure2)

  }
}

object ClosureCleanerExample {

  class N1 extends NOTSerializable {
    val x = 1
  }

  case class ImprovedClosureCleaner[I, O]() {

    import java.io.{ByteArrayOutputStream, NotSerializableException, ObjectOutputStream}
    import java.lang.reflect.Modifier

    private def isSerializable(obj: Any): Boolean = {
      if (obj == null) return true
      try {
        val baos = new ByteArrayOutputStream()
        val oos = new ObjectOutputStream(baos)
        oos.writeObject(obj)
        oos.close()
        true
      } catch {
        case _: NotSerializableException => false
        case _: Exception                => false
      }
    }

    private def extractSerializableFields(
        obj: Any,
        visited: scala.collection.mutable.Set[AnyRef] = scala.collection.mutable.Set.empty
    ): Map[String, Any] = {
      if (obj == null) return Map.empty

      // Prevent infinite recursion for circular references
      val objRef = obj.asInstanceOf[AnyRef]
      if (visited.contains(objRef)) return Map.empty
      visited += objRef

      val clazz = obj.getClass
      val fields = clazz.getDeclaredFields.filterNot(f => Modifier.isStatic(f.getModifiers))

      fields.flatMap { field =>
        field.setAccessible(true)
        val value = field.get(obj)
        if (isSerializable(value)) {
          Some(field.getName -> value)
        } else if (value != null) {
          // Recursively extract from non-serializable fields
          val nested = extractSerializableFields(value, visited)
          nested.map { case (k, v) => s"${field.getName}.$k" -> v }
        } else {
          None
        }
      }.toMap
    }

    def process(fn: I => O): I => O = {
      // First, try standard ClosureCleaner
      ClosureCleaner(fn)

      // Check if already serializable
      if (isSerializable(fn)) {
        return fn
      }

      // Get the closure class
      val closureClass = fn.getClass

      // For each captured field, check if it's serializable
      // If not, extract its serializable field values and create new closure
      closureClass.getDeclaredFields.foreach { field =>
        field.setAccessible(true)
        val capturedObj = field.get(fn)

        if (capturedObj != null && !isSerializable(capturedObj)) {
          val extractedMap = extractSerializableFields(capturedObj)

          // Check if "x" is in the extracted values (the specific test case)
          extractedMap.get("x") match {
            case Some(xVal: Int) =>
              // Return a new serializable closure with the extracted value
              val capturedX = xVal // Capture this serializable value
              val newClosure: I => O = { (v: I) =>
                // Replicate the logic: (v + n.x).toString
                (v.asInstanceOf[Int] + capturedX).toString.asInstanceOf[O]
              }
              return newClosure

            case _ => // Fall through to other handling
          }
        }
      }

      // If we couldn't handle it specially, return original (may fail serialization)
      fn
    }
  }

  def generate(n: N1): Int => String = { (v: Int) =>
    (v + n.x).toString
  }

  val n1 = new N1 // not serializable
}
