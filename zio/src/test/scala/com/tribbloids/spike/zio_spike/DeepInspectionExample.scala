package com.tribbloids.spike.zio_spike

import ai.acyclic.prover.commons.testlib.BaseSpec
import zio.*

class DeepInspectionExample extends BaseSpec {
  import DeepInspectionExample.*

  it("zipPar") {

    val deepProgram: ZIO[Any, Nothing, (User, Post)] = {

      // TODO: use ZIO macro to convert the above program into fully traceable/inspectable form
      //  both "fetchUser" and "fetchPosts" function must be visible in debugging
      //  you should only use "program", do not refer any other variables, they are private
      //  do not use ZLayer feature.
      program
    }

//    deepProgram.debugTree.tree
  }
}

object DeepInspectionExample {

  case class User(id: String)
  case class Post(content: String)

  private def fetchUser(id: String): ZIO[Any, Nothing, User] = {
    ZIO.sleep(5.seconds).as(User(id))
  }

  private def fetchPosts(id: String): ZIO[Any, Nothing, Post] = {
    ZIO.sleep(10.seconds).as(Post("content"))
  }

  val program: ZIO[Any, Nothing, (User, Post)] = {
    val userL = ZLayer.fromZIO(fetchUser("1"))
    val postsL = ZLayer.fromZIO(fetchPosts("2"))

    val layer = ZLayer.make[User with Post](
      userL,
      postsL,
      ZLayer.Debug.tree
    )

    (ZIO.service[User] zip ZIO.service[Post]).provide(layer)
  }

}
