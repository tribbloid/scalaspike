package scala.spike.RefinedType

object Example1 {

  val threeD = Dim(3)
  val fourD = Dim(3)

  trait SU_n[D <: Dim] {

    def plus(v1: Quaternion, v2: Quaternion)(
        implicit ev: D <:< threeD.type
    ): Quaternion = Quaternion(v1.w * v2.w)
  }

  case class Quaternion(w: Int) {}

  case class Dim(d: Int) {}

  val alsoThreeD = Dim(3)

  object SU_3 extends SU_n[alsoThreeD.type] {}
  object SU_4 extends SU_n[fourD.type] {}

  //TODO: how to fix it?
  val v3 = SU_3.plus(Quaternion(1), Quaternion(2))

  //this will trigger an error
  val v4 = SU_4.plus(Quaternion(1), Quaternion(2))
}
