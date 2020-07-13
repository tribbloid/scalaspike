package com.tribbloids.spike.utils.debug

import com.tribbloids.spike.utils.ScalaReflection.universe._

case class ShowType(tpe: Type) {

  lazy val baseTypes: List[Type] = {

    val baseClzs = tpe.baseClasses

    val result = baseClzs.map { clz =>
      tpe.baseType(clz)
    }
    result
  }

  override def toString: String = {

    s"""
       |$tpe
       |${baseTypes.map(v => "\t- " + v).mkString("\n")}
       """.trim.stripMargin
  }
}

object ShowType {

  def apply[T](implicit ttag: TypeTag[T]): ShowType = ShowType(ttag.tpe)

  def infer[T: TypeTag](v: T): ShowType = apply[T]
}
