package com.tribbloids.spike.scala_spike.reflection

import com.tribbloids.spike.scala_spike.reflection.TypeTag2Manifest.{
  example,
  toManifest
}
import org.scalatest.FunSpec

import scala.reflect.{ClassTag, ManifestFactory}

object TypeTag2Manifest {

  import org.apache.spark.sql.catalyst.ScalaReflection.universe._

  def toManifest[T: TypeTag]: Manifest[T] = {
    val t = typeTag[T]
    val mirror = t.mirror
    def toManifestRec(t: Type): Manifest[_] = {
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
    toManifestRec(t.tpe).asInstanceOf[Manifest[T]]
  }

  class Example {

    type T = Map[String, Int]
  }
  val example = new Example

}

class TypeTag2Manifest extends FunSpec {

  import org.apache.spark.sql.catalyst.ScalaReflection.universe._
  import TypeTag2Manifest._

  it("can convert") {

    val t1 = implicitly[TypeTag[example.T]]
    val v1 = toManifest(t1)
    val v2 = implicitly[Manifest[example.T]]

    assert(v1 == v2)
  }
}
