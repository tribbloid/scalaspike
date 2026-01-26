package com.tribbloids.spike.quill_spike

import io.getquill.{Literal, MirrorSqlDialect, SqlMirrorContext}
import org.scalatest.funspec.AnyFunSpec

case class Person(name: String, age: Int)
case class Address(personName: String, street: String)

class QuillASTExample extends AnyFunSpec {

  val ctx = new SqlMirrorContext(MirrorSqlDialect, Literal)

  describe("Quill AST inspection") {
    import ctx.*

    it("can access AST at runtime") {
      val q = quote {
        query[Person]
          .filter(p => p.age > 18)
          .flatMap { p =>
            query[Address]
              .filter { a =>
                a.personName == p.name
              }
              .map { a =>
                (p.name, a.street)
              }
          }
      }

//      val noQ =
//        query[Person]
//          .filter(p => p.age > 18)
//          .flatMap { p =>
//            query[Address]
//              .filter { a =>
//                a.personName == p.name
//              }
//              .map { a =>
//                (p.name, a.street)
//              }
//          }

      // Access the AST at runtime!
      val ast = q.ast
//      val noAst = noQ.ast

      println(ast)
//      println(noAst)
    }

    it("can be constructed manually") {
      import io.getquill.ast._
      import io.getquill.quat.Quat

      // Manually constructing the AST
      // This is a best-effort guess at the structure based on the query:
      // query[Person].filter(p => p.age > 18).flatMap(p => query[Address].filter(a => a.personName == p.name).map(a => (p.name, a.street)))

      // Quat is required for Entity. Using a dummy Product Quat.
      val personQuat = Quat.Product("Person", Quat.Product.Type.Concrete, scala.collection.mutable.LinkedHashMap.empty)
      val addressQuat =
        Quat.Product("Address", Quat.Product.Type.Concrete, scala.collection.mutable.LinkedHashMap.empty)

      val personEntity = Entity("Person", Nil, personQuat)
      val addressEntity = Entity("Address", Nil, addressQuat)

      val p = Ident("p")
      val a = Ident("a")

      // p.age > 18
      val filterBody1 = BinaryOperation(
        Property(p, "age"),
        NumericOperator.`>`,
        Constant.auto(18)
      )

      // a.personName == p.name
      val filterBody2 = BinaryOperation(
        Property(a, "personName"),
        EqualityOperator.`_==`,
        Property(p, "name")
      )

      // (p.name, a.street)
      val mapBody = Tuple(
        List(
          Property(p, "name"),
          Property(a, "street")
        )
      )

      val manualAst = FlatMap(
        Filter(personEntity, p, filterBody1),
        p,
        Map(
          Filter(addressEntity, a, filterBody2),
          a,
          mapBody
        )
      )

      val q = quote {
        query[Person]
          .filter(p => p.age > 18)
          .flatMap { p =>
            query[Address]
              .filter { a =>
                a.personName == p.name
              }
              .map { a =>
                (p.name, a.street)
              }
          }
      }

      println(s"Manual AST: $manualAst")
      println(s"Macro AST:  ${q.ast}")

      assert(q.ast == manualAst)
    }
  }
}
