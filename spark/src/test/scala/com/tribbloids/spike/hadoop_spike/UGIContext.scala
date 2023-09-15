package com.tribbloids.spike.hadoop_spike

import org.scalatest.funspec.AnyFunSpec

class UGIContext extends AnyFunSpec {

  it("in different thread") {

    import org.apache.hadoop.security.UserGroupInformation
    import java.security.PrivilegedAction

    val ugi = UserGroupInformation.createUserForTesting("abc", Array("a", "b", "c"))

    val u = UserGroupInformation.getCurrentUser
    println(u)

    val t1 = Thread.currentThread()

    val t2 = ugi.doAs {
      new PrivilegedAction[Thread] {
        override def run(): Thread = {

          val u = UserGroupInformation.getCurrentUser
          println(u)

          val t = Thread.currentThread()

          t
        }
      }
    }

    assert(t1 == t2)
  }
}
