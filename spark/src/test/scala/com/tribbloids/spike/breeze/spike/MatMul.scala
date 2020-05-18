package com.tribbloids.spike.breeze.spike

import org.scalatest.FunSpec


class MatMul extends FunSpec {

  import breeze.linalg._

  it("can multiply matrix") {

    val m1 = DenseMatrix.rand[Double](2, 3)

    val m2 = DenseMatrix.rand[Double](4,3)

    // No compilation error? Outrageous!!!
    val m3: DenseMatrix[Double] = m1 * m2

    val m4 = m1 * m2.t
  }
}
