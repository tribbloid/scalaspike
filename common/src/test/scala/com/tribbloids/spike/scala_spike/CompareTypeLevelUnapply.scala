package com.tribbloids.spike.scala_spike

import ai.acyclic.prover.commons.refl.Reflection.Runtime.TypeTag
import org.scalatest.funspec.AnyFunSpec
import shapeless.Typeable

import scala.reflect.ClassTag

class CompareTypeLevelUnapply extends AnyFunSpec {

  describe("Typeable") {

    def fwd(k: Typeable[?], v: Typeable[?]) = {
      (k, v) match {
        case (_: Typeable[a], _: Typeable[b]) =>
          implicitly[Typeable[Map[a, b]]]
      }
    }

    def rev[X, Y](m: Typeable[Map[X, Y]]) = {}
  }

  describe("TypeTag") {

    def fwd[X, Y](k: TypeTag[?], v: TypeTag[?]) = {
      (k, v) match {
        case (aT: TypeTag[a], bT: TypeTag[b]) =>
          implicit val _aT = aT
          implicit val _bT = bT
          implicitly[TypeTag[Map[a, b]]]
      }
    }
  }

  describe("ClassTag") {

    def fwd(k: ClassTag[?], v: ClassTag[?]) = {
      (k, v) match {
        case (_: ClassTag[a], _: ClassTag[b]) =>
          implicitly[ClassTag[Map[a, b]]]
      }
    }
  }
}
object CompareTypeLevelUnapply extends App {}
