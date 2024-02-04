package com.tribbloids.spike.scala_spike.Overriding

object CaseClassOverridingRule {

  {
    //  unary function (without eta-expansion) overriden by unary case class constructor

    trait A {
      type CC
      def CC: { def apply(v: Int): CC }
    }

    object AA extends A {

      case class CC(v: Int) {}
    }
  }

  {
    //  nullary function (without eta-expansion) overriden by nullary case class constructor

    trait A {
      type CC
      def CC: { def apply(): CC }
    }

    object AA extends A {

      case class CC() {}
    }
  }

  // WARNING! the following will be discarded by Scala 3
  {
    //  unary function (without eta-expansion) overriden by unary case class constructor

    trait A {
      type CC
      def CC: Int => CC // only works in Scala 2
    }

    object AA extends A {

      case class CC(v: Int) {}
    }
  }

  {
    //  nullary function (without eta-expansion) overriden by nullary case class constructor

    trait A {
      type CC
      def CC: () => CC // only works in Scala 2
    }

    object AA extends A {

      case class CC() {}
    }
  }
}
