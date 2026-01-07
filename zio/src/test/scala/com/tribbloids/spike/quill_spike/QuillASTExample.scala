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

      // Access the AST at runtime!
      val ast = q.ast
      println(ast)
    }
  }
}
