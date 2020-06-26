package com.tribbloids.spike

import ScalaReflection._

case class TypeView(tpe: universe.Type) {

  import universe._

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
