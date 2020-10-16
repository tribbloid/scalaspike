package com.tribbloids.spike.shapeless_spike

import com.tribbloids.spike.BaseSpec
import graph.commons.util.WideTyped
import graph.commons.util.debug.print_@

class RecordSuite extends BaseSpec {

  import shapeless._;
  import syntax.singleton._
  import record._

  val book =
    ("author" ->> "Benjamin Pierce") ::
      ("title" ->> "Types and Programming Languages") ::
      ("id" ->> 262162091) ::
      ("price" ->> 44.11) ::
      HNil

  it("can lookup") {

    print_@(WideTyped(book).viz)
  }
}
