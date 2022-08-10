package com.tribbloids.spike.scala_spike.Reflection

import org.scalatest.funspec.AnyFunSpec

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

class TypeTagFromType extends AnyFunSpec {

  import ai.acyclic.graph.commons.viz.TypeViz.universe._

  it("create TypeTag from reflection") {

    val ttg = typeTag[String]
    val ttg2 = TypeUtils.createTypeTag_fast(ttg.tpe, ttg.mirror)
    val ttg3 = TypeUtils.createTypeTag_slow(ttg.tpe, ttg.mirror)

    Seq(
      ttg -> "from static inference",
      ttg2 -> "from dynamic type - fast",
      ttg3 -> "from dynamic type - slow"
    ).map {
      case (tt, k) =>
        println(k)

        try {

          val bytes = serialise(tt)
          val tt2 = deserialise(bytes)
          assert(tt.tpe =:= tt2.tpe)
        } catch {
          case e: Throwable =>
            e.printStackTrace()
        }

    }
  }

  def serialise(tt: TypeTag[_]): Array[Byte] = {
    val bos = new ByteArrayOutputStream()
    try {
      val out = new ObjectOutputStream(bos)
      out.writeObject(tt)
      out.flush()
      val array = bos.toByteArray
      array
    } finally {
      bos.close()
    }
  }

  def deserialise(tt: Array[Byte]): TypeTag[_] = {

    val bis = new ByteArrayInputStream(tt)

    try {
      val in = new ObjectInputStream(bis)
      in.readObject().asInstanceOf[TypeTag[_]]

    } finally {
      bis.close()
    }

  }
}

object TypeUtils {

  import ai.acyclic.graph.commons.viz.TypeViz.universe._

  def createTypeTag_fast[T](
      tpe: Type,
      mirror: Mirror
  ): TypeTag[T] = {
    TypeTag.apply(
      mirror,
      NaiveTypeCreator(tpe)
    )
  }

  def createTypeTag_slow[T](
      tpe: Type,
      mirror: Mirror
  ): TypeTag[T] = {

    val toolbox = scala.tools.reflect.ToolBox(mirror).mkToolBox()

    val tree = toolbox.parse(s"scala.reflect.runtime.universe.typeTag[$tpe]")
    val result = toolbox.eval(tree).asInstanceOf[TypeTag[T]]

    result
  }

  case class NaiveTypeCreator(tpe: Type) extends reflect.api.TypeCreator {

    def apply[U <: reflect.api.Universe with Singleton](m: reflect.api.Mirror[U]): U#Type = {
      //          assert(m eq mirror, s"TypeTag[$tpe] defined in $mirror cannot be migrated to $m.")
      tpe.asInstanceOf[U#Type]
    }
  }
}
