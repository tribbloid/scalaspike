package com.tribbloids.spike.meta.multistage.circe

object RecursiveCaseClassDerivation {

  /* If Encoder[Foo] is not defined, compilation fails with
  [error] 7 |  given Encoder[Dto] = deriveEncoder[Dto]
  [error]   |                       ^^^^^^^^^^^^^^^^^^
  [error]   |  cannot reduce summonFrom with
  [error]   |   patterns :  case given encodeA @ _:io.circe.Encoder[Seq[Main.Foo]]
  [error]   |               case given evidence$1 @ _:deriving.Mirror.Of[Seq[Main.Foo]]
  [error]   | This location contains code that was inlined from Derivation.scala:14
  [error]   | This location contains code that was inlined from Derivation.scala:38
  [error]   | This location contains code that was inlined from Derivation.scala:12
  [error]   | This location contains code that was inlined from Derivation.scala:50
  [error]   | This location contains code that was inlined from semiauto.scala:27

  If Endoder[Foo] is defined, compilation fails with:
  [error] 18 |  given Encoder[Foo] = deriveEncoder[Foo]
  [error]    |                       ^^^^^^^^^^^^^^^^^^
  [error]    |                     Maximal number of successive inlines (32) exceeded,
  [error]    |                     Maybe this is caused by a recursive inline method?
  [error]    |                     You can use -Xmax-inlines to change the limit.
  [error]    | This location contains code that was inlined from Main.scala:18
  [error]    | This location contains code that was inlined from Derivation.scala:16
  [error]    | This location contains code that was inlined from Derivation.scala:38
  [error]    | This location contains code that was inlined from Derivation.scala:12
  [error]    | This location contains code that was inlined from Derivation.scala:50
  [error]    | This location contains code that was inlined from Derivation.scala:16
  [error]    | This location contains code that was inlined from Derivation.scala:38
  [error]    | This location contains code that was inlined from Derivation.scala:12
  [error]    | This location contains code that was inlined from Derivation.scala:50
  [error]    | This location contains code that was inlined from Derivation.scala:16
  [error]    | This location contains code that was inlined from Derivation.scala:38
  [error]    | This location contains code that was inlined from Derivation.scala:38
  [error]    | This location contains code that was inlined from Derivation.scala:12
  [error]    | This location contains code that was inlined from Derivation.scala:50
  [error]    | This location contains code that was inlined from Derivation.scala:16
  [error]    | This location contains code that was inlined from Derivation.scala:38
  [error]    | This location contains code that was inlined from Derivation.scala:12
  [error]    | This location contains code that was inlined from Derivation.scala:50
  [error]    | This location contains code that was inlined from Derivation.scala:16
  [error]    | This location contains code that was inlined from Derivation.scala:38
  [error]    | This location contains code that was inlined from Derivation.scala:12
  [error]    | This location contains code that was inlined from Derivation.scala:50
  [error]    | This location contains code that was inlined from Derivation.scala:16
  [error]    | This location contains code that was inlined from Derivation.scala:38
  [error]    | This location contains code that was inlined from Derivation.scala:38
  [error]    | This location contains code that was inlined from Derivation.scala:12
  [error]    | This location contains code that was inlined from Derivation.scala:50
  [error]    | This location contains code that was inlined from Derivation.scala:16
  [error]    | This location contains code that was inlined from Derivation.scala:38
  [error]    | This location contains code that was inlined from Derivation.scala:12
  [error]    | This location contains code that was inlined from Derivation.scala:50
  [error]    | This location contains code that was inlined from semiauto.scala:27
  [error] one error found
  [error] one error found
  [error] (Compile / compileIncremental) Compilation failed
   */

//  implicit val e1: Encoder[Foo] = deriveEncoder[Foo] // TODO: this needs to be fixed by Scala compiler
//  implicit val e2: Encoder[Dto] = deriveEncoder[Dto]

  case class Dto(elements: Seq[Foo])
  case class Foo(bar: Option[Bar])
  case class Bar(baz: Option[Baz])
  case class Baz(qux: Qux)
  case class Qux(a: Int)
}
