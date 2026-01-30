package com.tribbloids.spike.meta.fory

import com.google.protobuf.{Any => PbAny, ByteString}
import com.tribbloids.spike.meta.Fixture
import com.tribbloids.spike.meta.Fixture.{Msg, User}
import org.apache.fory.Fory
import org.apache.fory.config.Language
import org.apache.fory.serializer.scala.ScalaSerializers
import org.scalatest.funspec.AnyFunSpec

class ForyProtobufRoundtripExample extends AnyFunSpec {

  private lazy val fory = {
    val v = Fory
      .builder()
      .withLanguage(Language.JAVA)
      .requireClassRegistration(false)
      .build()

    ScalaSerializers.registerSerializers(v)

    v.register(classOf[User])
    v.register(classOf[Msg])

    v
  }

  private def toProtobuf(value: AnyRef): PbAny = {
    val bytes = fory.serialize(value)

    PbAny
      .newBuilder()
      .setTypeUrl(s"fory/${value.getClass.getName}")
      .setValue(ByteString.copyFrom(bytes))
      .build()
  }

  private def fromProtobuf[T](pb: PbAny): T = {
    fory.deserialize(pb.getValue.toByteArray).asInstanceOf[T]
  }

  describe("Apache Fory serialize -> protobuf envelope -> deserialize") {

    it("roundtrip Fixture.User") {
      val v1 = Fixture.User(
        name = "a",
        age = 1
      )

      val pb = toProtobuf(v1)
      val pbBytes = pb.toByteArray
      val pb2 = PbAny.parseFrom(pbBytes)

      val v2 = fromProtobuf[User](pb2)

      assert(v2 == v1)
    }

    it("roundtrip Fixture.Msg") {
      val v1 = Fixture.Msg(
        ulike = Fixture.User(
          name = "b",
          age = 2
        ),
        content = "hello"
      )

      val pb = toProtobuf(v1)
      val pbBytes = pb.toByteArray
      val pb2 = PbAny.parseFrom(pbBytes)

      val v2 = fromProtobuf[Msg](pb2)

      assert(v2 == v1)
    }
  }
}
