package com.tribbloids.spike.utils.debug

import com.tribbloids.spike.utils.ScalaReflection.universe._

case class ShowVal[T](
    value: T,
    inferredTTag: TypeTag[_],
    runtimeClass: Class[_]
) {

  override def toString: String = {
    s"""$value
         | : ${inferredTTag.tpe}
         | @ ${runtimeClass.getCanonicalName}
         """.trim.stripMargin
  }

  lazy val inferType: ShowType = {

    ShowType.apply(inferredTTag)
  }
}
