package com.tribbloids.spike.cats_spike

package object free {

  /**
    * Let me write the answer showing how to use Cats Free monad to define a lambda function like `{x: Int => 2*x + 1}`.
    *
    * ## Using Cats Free Monad to Define `{x: Int => 2*x + 1}`
    *
    * Free monads are typically used for building **DSLs that represent sequences of instructions**, not for directly
    * representing lambda functions. However, I can show you how to build an arithmetic expression DSL using Free monads
    * that captures the computation `2*x + 1`, which demonstrates the key concepts.
    *
    * ### The Key Insight
    *
    * A lambda like `x => 2*x + 1` is a function that takes a value and computes a result. With Free monads, instead of
    * defining the lambda directly, you **build an AST representing the computation**, then interpret it with different
    * values. [scalac](https://scalac.io/blog/free-monad-cats-overview/)
    *
    * ### Step 1: Define Your Algebra (ADT)
    *
    * First, create an algebraic data type representing arithmetic operations:
    * [github](https://github.com/typelevel/cats/issues/983)
    *
    * ```scala
    * import cats.free.Free
    * import cats.{~>, Id}
    *
    * // The functor representing our expression language
    * sealed trait ExprA[A]
    * case class Const(value: Int) extends ExprA[Int]
    * case class Add(left: Int, right: Int) extends ExprA[Int]
    * case class Multiply(left: Int, right: Int) extends ExprA[Int]
    * case class GetVar(name: String) extends ExprA[Int]
    * ```
    *
    * ### Step 2: Create the Free Type and Smart Constructors
    *
    * Lift your algebra into the Free monad context: [typelevel](https://typelevel.org/cats/datatypes/freemonad.html)
    *
    * ```scala
    * type Expr[A] = Free[ExprA, A]
    *
    * import cats.free.Free.liftF
    *
    * // Smart constructors
    * def const(v: Int): Expr[Int] =
    *   liftF[ExprA, Int](Const(v))
    *
    * def add(left: Int, right: Int): Expr[Int] =
    *   liftF[ExprA, Int](Add(left, right))
    *
    * def multiply(left: Int, right: Int): Expr[Int] =
    *   liftF[ExprA, Int](Multiply(left, right))
    *
    * def getVar(name: String): Expr[Int] =
    *   liftF[ExprA, Int](GetVar(name))
    * ```
    *
    * ### Step 3: Build Your Program (AST for `2*x + 1`)
    *
    * Now compose your expression using for-comprehension: [github](https://github.com/typelevel/cats/issues/983)
    *
    * ```scala
    * // Represents: x => 2*x + 1
    * def program: Expr[Int] = for {
    *   x <- getVar("x") // Get variable x
    *   two <- const(2) // Constant 2
    *   twoX <- multiply(two, x) // 2 * x
    *   one <- const(1) // Constant 1
    *   result <- add(twoX, one) // (2*x) + 1
    * } yield result
    * ```
    *
    * This doesn't execute anything—it builds a pure data structure describing the computation.
    * [softwaremill](https://softwaremill.com/free-monads/)
    *
    * ### Step 4: Write an Interpreter
    *
    * Create a natural transformation to execute your program:
    * [typelevel](https://typelevel.org/cats/datatypes/freemonad.html)
    *
    * ```scala
    * import scala.collection.mutable
    *
    * // Interpreter that uses a variable environment
    * class Evaluator(env: Map[String, Int]) extends (ExprA ~> Id) {
    *   def apply[A](fa: ExprA[A]): Id[A] = fa match {
    *     case Const(v)       => v
    *     case Add(l, r)      => l + r
    *     case Multiply(l, r) => l * r
    *     case GetVar(name)   => env.getOrElse(name, 0)
    *   }
    * }
    * ```
    *
    * ### Step 5: Execute the Program
    *
    * Run your program by folding it with the interpreter: [github](https://github.com/typelevel/cats/issues/983)
    *
    * ```scala
    * // Execute with x = 5
    * val interpreter = new Evaluator(Map("x" -> 5))
    * val result: Int = program.foldMap(interpreter)
    * // result: Int = 11  (2*5 + 1 = 11)
    *
    * // Execute with x = 10
    * val interpreter2 = new Evaluator(Map("x" -> 10))
    * val result2: Int = program.foldMap(interpreter2)
    * // result2: Int = 21  (2*10 + 1 = 21)
    * ```
    *
    * ### Complete Working Example
    *
    * Here's the full code: [scalac](https://scalac.io/blog/free-monad-cats-overview/)
    *
    * ```scala
    * import cats.free.Free
    * import cats.{~>, Id}
    *
    * // 1. Define the algebra
    * sealed trait ExprA[A]
    * case class Const(value: Int) extends ExprA[Int]
    * case class Add(left: Int, right: Int) extends ExprA[Int]
    * case class Multiply(left: Int, right: Int) extends ExprA[Int]
    * case class GetVar(name: String) extends ExprA[Int]
    *
    * // 2. Create Free type and smart constructors
    * type Expr[A] = Free[ExprA, A]
    *
    * import cats.free.Free.liftF
    *
    * def const(v: Int): Expr[Int] = liftF[ExprA, Int](Const(v))
    * def add(left: Int, right: Int): Expr[Int] = liftF[ExprA, Int](Add(left, right))
    * def multiply(left: Int, right: Int): Expr[Int] = liftF[ExprA, Int](Multiply(left, right))
    * def getVar(name: String): Expr[Int] = liftF[ExprA, Int](GetVar(name))
    *
    * // 3. Build the program: 2*x + 1
    * def program: Expr[Int] = for {
    *   x <- getVar("x")
    *   two <- const(2)
    *   twoX <- multiply(two, x)
    *   one <- const(1)
    *   result <- add(twoX, one)
    * } yield result
    *
    * // 4. Write interpreter
    * class Evaluator(env: Map[String, Int]) extends (ExprA ~> Id) {
    *   def apply[A](fa: ExprA[A]): Id[A] = fa match {
    *     case Const(v)       => v
    *     case Add(l, r)      => l + r
    *     case Multiply(l, r) => l * r
    *     case GetVar(name)   => env.getOrElse(name, 0)
    *   }
    * }
    *
    * // 5. Execute
    * val result = program.foldMap(new Evaluator(Map("x" -> 5)))
    * // result = 11
    * ```
    *
    * ### Why This Approach?
    *
    * This Free monad approach gives you:
    * [deque](https://deque.blog/2017/11/13/free-monads-from-basics-up-to-implementing-composable-and-effectful-stream-processing/)
    *
    *   1. **Separation of concerns**: Program logic (step 3) is separated from execution (step 4)
    *   2. **Multiple interpreters**: You could write a different interpreter that prints the AST, optimizes it, or
    *      compiles to another representation [tweag](https://tweag.io/blog/2018-02-05-free-monads/)
    *   3. **Testability**: Mock interpreters for testing without executing real effects
    *      [blog.ploeh](https://blog.ploeh.dk/2017/08/07/f-free-monad-recipe/)
    *   4. **Composition**: Easily combine multiple DSLs using `Coproduct` or `EitherK`
    *      [scalac](https://scalac.io/blog/free-monad-cats-overview/)
    *
    * ### Important Note
    *
    * Free monads don't naturally express **first-class functions** (lambda abstractions like `x => ...`). They're
    * designed for **building computation sequences**. If you need true lambda calculus with variable binding and
    * substitution, you'd need additional machinery like the `bound` library or higher-order abstract syntax (HOAS).
    * [interjectedfuture](https://interjectedfuture.com/how-the-free-monad-and-functors-represent-syntax/)
    *
    * The example above represents the **computation** `2*x + 1` as data, which can be interpreted with different
    * variable environments—functionally equivalent to calling a lambda with different arguments, but structured
    * differently.
    */
}
