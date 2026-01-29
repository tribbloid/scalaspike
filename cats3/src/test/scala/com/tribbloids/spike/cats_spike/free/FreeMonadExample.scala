package com.tribbloids.spike.cats_spike.free

import cats.free.Free
import cats.{Id, ~>}
import cats.free.Free.liftF

object FreeMonadExample {

  // 1. Define the algebra
  sealed trait ExprA[A]
  case class Const(value: Int) extends ExprA[Int]
  case class Add(left: Int, right: Int) extends ExprA[Int]
  case class Multiply(left: Int, right: Int) extends ExprA[Int]
  case class GetVar(name: String) extends ExprA[Int]

  // 2. Create Free type and smart constructors
  type Expr[A] = Free[ExprA, A]


  def const(v: Int): Expr[Int] = liftF[ExprA, Int](Const(v))
  def add(left: Int, right: Int): Expr[Int] = liftF[ExprA, Int](Add(left, right))
  def multiply(left: Int, right: Int): Expr[Int] = liftF[ExprA, Int](Multiply(left, right))
  def getVar(name: String): Expr[Int] = liftF[ExprA, Int](GetVar(name))

  // 3. Build the program: 2*x + 1
  def program: Expr[Int] = for {
    x <- getVar("x")
    two <- const(2)
    twoX <- multiply(two, x)
    one <- const(1)
    result <- add(twoX, one)
  } yield result

  // 4. Write interpreter
  class Evaluator(env: Map[String, Int]) extends (ExprA ~> Id) {
    def apply[A](fa: ExprA[A]): Id[A] = fa match {
      case Const(v)       => v
      case Add(l, r)      => l + r
      case Multiply(l, r) => l * r
      case GetVar(name)   => env.getOrElse(name, 0)
    }
  }

  // 5. Execute
  def main(args: Array[String]): Unit = {
    val result = program.foldMap(new Evaluator(Map("x" -> 5)))
    println(s"result = $result")
    // result = 11
  }
}
