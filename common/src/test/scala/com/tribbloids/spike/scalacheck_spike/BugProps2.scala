package com.tribbloids.spike.scalacheck_spike

import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

class BugProps2 extends Properties("Bug") {

  propertyWithSeed("filtered lists are short", None) = forAll { (xs: List[Int], p: Int => Boolean) =>
    xs.count(p) < 60
  }

  propertyWithSeed.update(
    "filtered lists are short",
    None, {
      forAll { (xs: List[Int], p: Int => Boolean) =>
        xs.count(p) < 60
      }
    }
  )
}
