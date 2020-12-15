package com.tribbloids.spike.shapeless_spike

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.WideTyped
import com.tribbloids.graph.commons.util.debug.print_@

class RecordSuite extends BaseSpec {

  import shapeless._
  import syntax.singleton._

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
