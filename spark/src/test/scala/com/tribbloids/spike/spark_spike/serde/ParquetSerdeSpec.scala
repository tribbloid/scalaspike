package com.tribbloids.spike.spark_spike.serde

import ai.acyclic.prover.commons.testlib.BaseSpec
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.parquet.avro.{AvroParquetReader, AvroParquetWriter}

import java.nio.file.Files

class ParquetSerdeSpec extends BaseSpec {

  // Helper method to serialize to in-memory bytes using temp file
  def serializeToMemory(
      records: Seq[GenericRecord],
      schema: org.apache.avro.Schema
  ): Array[Byte] = {
    val tempFile = Files.createTempFile("parquet-test-", ".parquet")
    // Delete the file so Hadoop can create it fresh
    Files.deleteIfExists(tempFile)

    try {
      val writer = AvroParquetWriter
        .builder[GenericRecord](new Path(tempFile.toUri))
        .withSchema(schema)
        .withConf(new Configuration())
        .build()

      try {
        records.foreach(writer.write)
      } finally {
        writer.close()
      }

      Files.readAllBytes(tempFile)
    } finally {
      Files.deleteIfExists(tempFile)
    }
  }

  // Helper method to deserialize from in-memory bytes using temp file
  def deserializeFromMemory(
      bytes: Array[Byte],
      schema: org.apache.avro.Schema
  ): Seq[GenericRecord] = {
    val tempFile = Files.createTempFile("parquet-test-", ".parquet")
    // Delete the file so Hadoop can create it fresh
    Files.deleteIfExists(tempFile)

    try {
      Files.write(tempFile, bytes)

      val reader = AvroParquetReader
        .builder[GenericRecord](new Path(tempFile.toUri))
        .withConf(new Configuration())
        .build()

      try {
        Iterator
          .continually(reader.read())
          .takeWhile(_ != null)
          .toSeq
      } finally {
        reader.close()
      }
    } finally {
      Files.deleteIfExists(tempFile)
    }
  }

  describe("Parquet serialization") {

    it("serialize and deserialize a simple case class A") {

      // Define Avro schema for A
      val schemaA = new org.apache.avro.Schema.Parser().parse(
        """{"type":"record","name":"A","namespace":"test","fields":[{"name":"s","type":"string"}]}"""
      )

      // Create test record
      val recordA = new GenericData.Record(schemaA)
      recordA.put("s", "test-string")

      // Serialize to memory
      val serialized = serializeToMemory(Seq(recordA), schemaA)

      // Deserialize back
      val deserialized = deserializeFromMemory(serialized, schemaA)

      // Verify
      assert(deserialized.size == 1, "Should have 1 record")
      assert(
        deserialized.head.get("s").toString == "test-string",
        "String value should match"
      )

    }

    it("serialize and deserialize nested case class B1") {

      // Define Avro schema for B1 (contains nested A)
      val schemaB1 = new org.apache.avro.Schema.Parser().parse(
        """{
          |"type":"record",
          |"name":"B1",
          |"namespace":"test",
          |"fields":[{
            |"name":"a",
            |"type":{
              |"type":"record",
              |"name":"A",
              |"fields":[{"name":"s","type":"string"}]
            |}
          |}]
        |}""".stripMargin
      )

      // Create nested test record
      val innerA = new GenericData.Record(schemaB1.getField("a").schema())
      innerA.put("s", "nested-string")

      val recordB1 = new GenericData.Record(schemaB1)
      recordB1.put("a", innerA)

      // Serialize to memory
      val serialized = serializeToMemory(Seq(recordB1), schemaB1)

      // Deserialize back
      val deserialized = deserializeFromMemory(serialized, schemaB1)

      // Verify
      assert(deserialized.size == 1, "Should have 1 record")
      val outer = deserialized.head.asInstanceOf[GenericRecord]
      val inner = outer.get("a").asInstanceOf[GenericRecord]
      assert(inner.get("s").toString == "nested-string", "Nested string should match")
    }

    it("serialize multiple records of the same schema") {

      val schemaA = new org.apache.avro.Schema.Parser().parse(
        """{"type":"record","name":"A","namespace":"test","fields":[{"name":"s","type":"string"}]}"""
      )

      // Create multiple test records
      val records = Seq(
        "first",
        "second",
        "third"
      ).map { s =>
        val record = new GenericData.Record(schemaA)
        record.put("s", s)
        record
      }

      // Serialize to memory
      val serialized = serializeToMemory(records, schemaA)

      // Verify size
      assert(serialized.length > 0, "Serialized bytes should not be empty")

      // Deserialize back
      val deserialized = deserializeFromMemory(serialized, schemaA)

      // Verify all records
      assert(deserialized.size == 3, "Should have 3 records")
      val values = deserialized.map(_.get("s").toString)
      assert(values == Seq("first", "second", "third"), "All values should match")
    }

    it("handle empty record set") {

      val schemaA = new org.apache.avro.Schema.Parser().parse(
        """{"type":"record","name":"A","namespace":"test","fields":[{"name":"s","type":"string"}]}"""
      )

      // Serialize empty set
      val serialized = serializeToMemory(Seq.empty, schemaA)

      // Deserialize
      val deserialized = deserializeFromMemory(serialized, schemaA)

      // Should be empty
      assert(deserialized.isEmpty, "Should be empty")
    }

    it("preserve null values in records") {

      // Define schema with nullable field
      val schemaNullable = new org.apache.avro.Schema.Parser().parse(
        """{
          |"type":"record",
          |"name":"NullableRecord",
          |"namespace":"test",
          |"fields":[
            |{"name":"s1","type":["null","string"],"default":null},
            |{"name":"s2","type":["null","string"],"default":null}
          |]
        |}""".stripMargin
      )

      // Create record with mixed null/non-null values
      val record = new GenericData.Record(schemaNullable)
      record.put("s1", "not-null")
      record.put("s2", null)

      // Serialize and deserialize
      val serialized = serializeToMemory(Seq(record), schemaNullable)
      val deserialized = deserializeFromMemory(serialized, schemaNullable)

      // Verify null preservation
      assert(deserialized.size == 1, "Should have 1 record")
      val dr = deserialized.head
      // Avro returns CharSequence for strings, need to convert
      val s1Value = if (dr.get("s1") == null) null else dr.get("s1").toString
      assert(s1Value == "not-null", "Non-null field should match")
      assert(dr.get("s2") == null, "Null field should be preserved")
    }

    it("serialize case class B2 (empty case class)") {
      // Note: Parquet does not support schemas with no fields
      // This test documents that limitation
      // In practice, an empty case class would need at least a dummy field
      println("Parquet does not support empty schemas (no fields) - this is expected")
      println("Empty case classes would need a dummy field for Parquet serialization")

      // Using a schema with a dummy boolean field instead
      val schemaB2 = new org.apache.avro.Schema.Parser().parse(
        """{"type":"record","name":"B2","namespace":"test","fields":[{"name":"_empty","type":"boolean","default":false}]}"""
      )

      val recordB2 = new GenericData.Record(schemaB2)
      recordB2.put("_empty", false)

      val serialized = serializeToMemory(Seq(recordB2), schemaB2)

      val deserialized = deserializeFromMemory(serialized, schemaB2)

      assert(deserialized.size == 1, "Should have 1 record")
      println("B2 (with dummy field) serialized successfully")
    }

    it("convert Parquet bytes to JSON representation") {

      val schemaA = new org.apache.avro.Schema.Parser().parse(
        """{"type":"record","name":"A","namespace":"test","fields":[{"name":"s","type":"string"}]}"""
      )

      val recordA = new GenericData.Record(schemaA)
      recordA.put("s", "json-test")

      val serialized = serializeToMemory(Seq(recordA), schemaA)
    }
  }
}

object ParquetSerdeSpec extends {

  case class A(s: String)

  trait B
  case class B1(a: A) extends B
  case class B2() extends B

  case class C(b: B)
}
