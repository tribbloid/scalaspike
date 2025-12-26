package com.tribbloids.spike.deeplearning

import org.nd4j.autodiff.functions.DifferentialFunction
import org.nd4j.autodiff.samediff.{SDVariable, SameDiff}
import org.nd4j.linalg.api.buffer.DataType
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class SameDiffAutoDiffExample1 extends AnyFlatSpec {

  import org.scalatest.matchers.should.Matchers.*

  it should "compute gradients using SameDiff" in {

    val x0 = 2.0f

    val sd: SameDiff = SameDiff.create()

    val math = sd.math()

    val x: SDVariable = sd.`var`("x")
    x.setArray(Nd4j.scalar(DataType.FLOAT, x0))

    val three = sd.constant("three", Nd4j.scalar(DataType.FLOAT, 3.0f))

    val y = math.square(x).add(x.mul(three))
    val loss = y.mean("loss")

    printTree(loss)

    sd.setLossVariables("loss")

    val placeholders: java.util.Map[String, INDArray] = new java.util.HashMap[String, INDArray]()
    val gradMap = sd.calculateGradients(placeholders, "x")

    val grad = gradMap.get("x").getFloat(0L)
    val expected = 2.0f * x0 + 3.0f

    grad shouldBe expected +- 1e-4f
  }

  private def printTree(root: SDVariable): Unit = {

    val visitedVars = mutable.HashSet.empty[String]
    val visitedOps = mutable.HashSet.empty[String]

    def renderVar(v: SDVariable, indent: String): Unit = {
      val name = v.name()
      println(s"${indent}var ${name} : ${v.getVariableType}")
      if (visitedVars.contains(name)) return
      visitedVars += name

      val creator = v.getCreator
      if (creator != null) {
        renderOp(creator, indent + "  ")
      }
    }

    def renderOp(op: DifferentialFunction, indent: String): Unit = {
      val ownName = op.getOwnName
      println(s"${indent}op ${ownName} : ${op.opName()}")
      if (visitedOps.contains(ownName)) return
      visitedOps += ownName

      val args = Option(op.args()).getOrElse(Array.empty)
      args.foreach { vv =>
        if (vv != null) {
          renderVar(vv, indent + "  ")
        }
      }
    }

    renderVar(root, "")
  }
}
