package com.tribbloids.spike.xsource3.shapeless

object PolyCaseWithoutImport {

  def scope1(): Unit = {

    object Example extends shapeless.Poly1 {

      implicit def default[T]: Case.Aux[T, List[T]] = at[T] { v =>
        List(v)
      }
    }

    {
      val rr = Example("abc")

      println(rr)
    }

//    {
//      import Example.*
//      val rr = Example.apply("abc")
//
//      println(rr)
//    }

  }

//  {
//    val example = new shapeless.Poly1 {
//
//      implicit def default[T]: Case.Aux[T, List[T]] = at[T] { v =>
//        List(v)
//      }
//    }
//
//    {
//      val rr = example("abc")
//
//      println(rr)
//    }
//  }
}
