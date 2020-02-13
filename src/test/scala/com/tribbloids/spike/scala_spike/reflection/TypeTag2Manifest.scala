package com.tribbloids.spike.scala_spike.reflection

import org.scalatest.FunSpec

import scala.reflect.{ClassTag, ManifestFactory}

object TypeTag2Manifest {

  import org.apache.spark.sql.catalyst.ScalaReflection.universe._

  def toManifest[T: TypeTag]: Manifest[T] = {
    val tt = typeTag[T]
    val mirror = tt.mirror
    def toManifestRec(t: Type): Manifest[_] = {

      t.finalResultType
      val clazz = ClassTag[T](mirror.runtimeClass(t)).runtimeClass
      if (t.typeArgs.length == 1) {
        val arg = toManifestRec(t.typeArgs.head)
        ManifestFactory.classType(clazz, arg)
      } else if (t.typeArgs.length > 1) {
        val args = t.typeArgs.map(x => toManifestRec(x))
        ManifestFactory.classType(clazz, args.head, args.tail: _*)
      } else {
        ManifestFactory.classType(clazz)
      }
    }
    toManifestRec(tt.tpe).asInstanceOf[Manifest[T]]
  }

  class Example {

    type T = Map[String, Int]
  }
  val example = new Example

}

class TypeTag2Manifest extends FunSpec {

  import TypeTag2Manifest._
  import org.apache.spark.sql.catalyst.ScalaReflection.universe._

  it("can convert") {

    val t1 = implicitly[TypeTag[example.T]]
    val v1 = toManifest(t1)
    val v2 = implicitly[Manifest[example.T]]

    assert(v1 == v2)
  }
}
