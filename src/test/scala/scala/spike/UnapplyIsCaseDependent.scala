package scala.spike

object UnapplyIsCaseDependent {

  val Seq(a, b, c) = Seq(1, 2, 3)

  print(a + b + c)

  // this doesn't compile
//  val Seq(A, B, C) = Seq(1, 2, 3)
}
