package com.tribbloids.spike.zio_spike

// this is a TupleUnpacker that avoid any shapeless feature, much longer than necessary
trait TupleUnpacker[A] {
  type Head
  type Tail
}

object TupleUnpacker extends TupleUnpackerLowPriority {

  type Aux[A, H, T] = TupleUnpacker[A] { type Head = H; type Tail = T }

  def apply[A](
      implicit
      u: TupleUnpacker[A]
  ): Aux[A, u.Head, u.Tail] = u

  implicit def tuple2[A, B]: Aux[(A, B), A, B] = new TupleUnpacker[(A, B)] {
    type Head = A
    type Tail = B
  }

  implicit def tuple3[A, B, C]: Aux[(A, B, C), A, (B, C)] = new TupleUnpacker[(A, B, C)] {
    type Head = A
    type Tail = (B, C)
  }

  implicit def tuple4[A, B, C, D]: Aux[(A, B, C, D), A, (B, C, D)] = new TupleUnpacker[(A, B, C, D)] {
    type Head = A
    type Tail = (B, C, D)
  }

  implicit def tuple5[A, B, C, D, E]: Aux[(A, B, C, D, E), A, (B, C, D, E)] = new TupleUnpacker[(A, B, C, D, E)] {
    type Head = A
    type Tail = (B, C, D, E)
  }

  implicit def tuple6[A, B, C, D, E, F]: Aux[(A, B, C, D, E, F), A, (B, C, D, E, F)] =
    new TupleUnpacker[(A, B, C, D, E, F)] {
      type Head = A
      type Tail = (B, C, D, E, F)
    }

  implicit def tuple7[A, B, C, D, E, F, G]: Aux[(A, B, C, D, E, F, G), A, (B, C, D, E, F, G)] =
    new TupleUnpacker[(A, B, C, D, E, F, G)] {
      type Head = A
      type Tail = (B, C, D, E, F, G)
    }

  implicit def tuple8[A, B, C, D, E, F, G, H]: Aux[(A, B, C, D, E, F, G, H), A, (B, C, D, E, F, G, H)] =
    new TupleUnpacker[(A, B, C, D, E, F, G, H)] {
      type Head = A
      type Tail = (B, C, D, E, F, G, H)
    }

  implicit def tuple9[A, B, C, D, E, F, G, H, I]: Aux[(A, B, C, D, E, F, G, H, I), A, (B, C, D, E, F, G, H, I)] =
    new TupleUnpacker[(A, B, C, D, E, F, G, H, I)] {
      type Head = A
      type Tail = (B, C, D, E, F, G, H, I)
    }

  implicit def tuple10[A, B, C, D, E, F, G, H, I, J]: Aux[
    (A, B, C, D, E, F, G, H, I, J),
    A,
    (B, C, D, E, F, G, H, I, J)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J)
  }

  implicit def tuple11[A, B, C, D, E, F, G, H, I, J, K]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K),
    A,
    (B, C, D, E, F, G, H, I, J, K)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K)
  }

  implicit def tuple12[A, B, C, D, E, F, G, H, I, J, K, L]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L),
    A,
    (B, C, D, E, F, G, H, I, J, K, L)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L)
  }

  implicit def tuple13[A, B, C, D, E, F, G, H, I, J, K, L, M]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L, M),
    A,
    (B, C, D, E, F, G, H, I, J, K, L, M)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L, M)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L, M)
  }

  implicit def tuple14[A, B, C, D, E, F, G, H, I, J, K, L, M, N]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N),
    A,
    (B, C, D, E, F, G, H, I, J, K, L, M, N)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L, M, N)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L, M, N)
  }

  implicit def tuple15[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O),
    A,
    (B, C, D, E, F, G, H, I, J, K, L, M, N, O)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L, M, N, O)
  }

  implicit def tuple16[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P),
    A,
    (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P)
  }

  implicit def tuple17[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q),
    A,
    (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q)
  }

  implicit def tuple18[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R),
    A,
    (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R)
  }

  implicit def tuple19[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S),
    A,
    (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S)
  }

  implicit def tuple20[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T),
    A,
    (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T)
  }

  implicit def tuple21[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U),
    A,
    (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U)
  }

  implicit def tuple22[A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V]: Aux[
    (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V),
    A,
    (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)
  ] = new TupleUnpacker[(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)] {
    type Head = A
    type Tail = (B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V)
  }
}

trait TupleUnpackerLowPriority {
  implicit def default[A]: TupleUnpacker.Aux[A, A, Unit] = new TupleUnpacker[A] {
    type Head = A
    type Tail = Unit
  }
}
