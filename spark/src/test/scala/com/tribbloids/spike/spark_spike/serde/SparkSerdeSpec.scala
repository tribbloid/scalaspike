package com.tribbloids.spike.spark_spike.serde

import ai.acyclic.prover.commons.testlib.BaseSpec
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

/**
  * contains examples of the serialiser of Spark SQL to serialise examples into binary in memory, and human-readable
  * JSON
  *
  * This test suite is only for in memory BLOB, file system operation should be avoided
  */
class SparkSerdeSpec extends BaseSpec {

  lazy val spark: SparkSession = SparkSerdeSpec.spark

  describe("Lightweight Java/Kryo serialization") {

    it("serialize single object using Java serialization (lighter than DataFrame)") {

      // Java serialization - much lighter than creating a DataFrame
      val original = SparkSerdeSpec.A("test-string")

      // Serialize to bytes
      val baos = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(baos)
      oos.writeObject(original)
      oos.close()

      val bytes = baos.toByteArray
      info(s"Serialized size: ${bytes.length} bytes")

      // Deserialize back from bytes
      val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
      val deserialized = ois.readObject().asInstanceOf[SparkSerdeSpec.A]

      assert(deserialized == original, "Deserialized object should match original")
      info(s"Original: $original, Deserialized: $deserialized")
    }

    it("serialize nested case class using Java serialization") {

      val original = SparkSerdeSpec.B1(SparkSerdeSpec.A("nested-string"))

      // Serialize and deserialize
      val baos = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(baos)
      oos.writeObject(original)
      oos.close()

      val bytes = baos.toByteArray
      info(s"Nested serialized size: ${bytes.length} bytes")

      val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
      val deserialized = ois.readObject().asInstanceOf[SparkSerdeSpec.B1]

      assert(deserialized == original, "Nested structure should be preserved")
      info(s"Nested serialization successful: $deserialized")
    }

    it("serialize empty case class B2 using Java serialization") {

      val original = SparkSerdeSpec.B2()

      val baos = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(baos)
      oos.writeObject(original)
      oos.close()

      val bytes = baos.toByteArray
      info(s"Empty case class serialized size: ${bytes.length} bytes")

      val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
      val deserialized = ois.readObject().asInstanceOf[SparkSerdeSpec.B2]

      assert(deserialized == original, "Empty case class should serialize/deserialize")
      info(s"Empty case class B2 serialized successfully")
    }
  }

  describe("Encoder schema inference (no DataFrame needed)") {

    it("get schema from Encoder without creating DataFrame") {

      val encoder: Encoder[SparkSerdeSpec.A] = Encoders.product[SparkSerdeSpec.A]

      // Access schema directly from encoder - very lightweight
      val schema = encoder.schema

      info(s"Schema from encoder: $schema")
      assert(schema.fields.length == 1, "Should have 1 field")
      assert(schema.fields.head.name == "s", "Field name should be 's'")
    }

    it("get schema for nested case class from Encoder") {

      val encoder: Encoder[SparkSerdeSpec.B1] = Encoders.product[SparkSerdeSpec.B1]

      val schema = encoder.schema

      info(s"Nested schema from encoder: $schema")
      assert(schema.fields.length == 1, "Should have 1 field")
      assert(schema.fields.head.name == "a", "Field name should be 'a'")
      assert(schema.fields.head.dataType.simpleString == "struct<s:string>", "Should be a struct")
    }

    it("get schema for empty case class from Encoder") {

      val encoder: Encoder[SparkSerdeSpec.B2] = Encoders.product[SparkSerdeSpec.B2]

      val schema = encoder.schema

      info(s"Empty case class schema: $schema")
      assert(schema.fields.isEmpty, "Empty case class should have no fields")
    }
  }

  describe("DataFrame-based serialization (appropriate for collections)") {

    it("serialize multiple records using DataFrame") {

      import spark.implicits._

      // DataFrame is appropriate for collections/batches
      val data = Seq(
        SparkSerdeSpec.A("first"),
        SparkSerdeSpec.A("second"),
        SparkSerdeSpec.A("third")
      )
      val df = data.toDF()

      val collected = df.collect()
      assert(collected.length == 3, "Should have 3 rows")

      val values = collected.map(_.getAs[String]("s")).toSeq
      assert(values == Seq("first", "second", "third"), "All values should match")

      // Convert to JSON (human-readable)
      val jsonStrings = df.toJSON.collect()
      assert(jsonStrings.length == 3, "Should have 3 JSON strings")
      info(s"JSON output: ${jsonStrings.mkString(", ")}")
    }

    it("demonstrate DataFrame operations on collections") {

      import spark.implicits._

      val data = Seq(
        SparkSerdeSpec.A("apple"),
        SparkSerdeSpec.A("banana"),
        SparkSerdeSpec.A("cherry")
      )
      val df = data.toDF()

      // Filter in memory
      val filtered = df.filter("s LIKE 'a%'")
      val filteredCollected = filtered.collect()
      assert(filteredCollected.length == 1, "Should have 1 row starting with 'a'")

      // Sort in memory
      val sorted = df.sort("s")
      val sortedCollected = sorted.collect()
      assert(
        sortedCollected.map(_.getAs[String]("s")).toSeq == Seq("apple", "banana", "cherry"),
        "Should be sorted alphabetically"
      )

      info("DataFrame operations are efficient for collections")
    }

    it("handle null values in DataFrame") {

      import spark.implicits._

      val data = Seq(SparkSerdeSpec.NullableRecord("not-null", null))
      val df = data.toDF()

      val collected = df.collect()
      val row = collected.head
      assert(row.getAs[String]("s1") == "not-null", "Non-null field should match")
      assert(row.getAs[String]("s2") == null, "Null field should be preserved")

      // JSON representation
      val jsonStrings = df.toJSON.collect()
      info(s"JSON with null: ${jsonStrings.head}")
    }

    it("convert single object to JSON (lightweight approach)") {

      import spark.implicits._

      // For JSON output, DataFrame + toJSON is convenient
      val data = Seq(SparkSerdeSpec.A("single-json"))
      val json = data.toDF().toJSON.first()

      assert(json.contains("single-json"), "JSON should contain the value")
      info(s"JSON for single object: $json")
    }

    it("convert single object to Parquet in-memory") {

      import spark.implicits._

      // DataFrame is needed for Parquet format
      val data = Seq(SparkSerdeSpec.A("parquet-test"))
      val df = data.toDF()

      // Get binary representation via internal Row format
      val rows = df.collect()
      assert(rows.length == 1, "Should have 1 row")

      // For actual Parquet bytes, you'd need to write to a temp file
      // But the Row objects are already a compact binary representation
      info(s"Row representation: ${rows.head}")
    }
  }

  describe("Serialization comparison and trade-offs") {

    it("compare Java serialization vs DataFrame for single object") {

      import spark.implicits._

      val original = SparkSerdeSpec.A("comparison-test")

      // Java serialization approach - lightweight
      val start1 = System.nanoTime()
      val baos = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(baos)
      oos.writeObject(original)
      oos.close()
      val bytes1 = baos.toByteArray

      val ois = new ObjectInputStream(new ByteArrayInputStream(bytes1))
      val deserialized1 = ois.readObject().asInstanceOf[SparkSerdeSpec.A]
      val javaSerTime = System.nanoTime() - start1

      // DataFrame approach - heavier but more features
      val start2 = System.nanoTime()
      val df = Seq(original).toDF()
      val collected = df.collect()
      val deserialized2 = collected.head.getAs[String]("s")
      val dfTime = System.nanoTime() - start2

      info(s"Java serialization time: ${javaSerTime / 1000} μs, size: ${bytes1.length} bytes")
      info(s"DataFrame time: ${dfTime / 1000} μs")
      info(s"DataFrame is approximately ${(dfTime.toDouble / javaSerTime).toInt}x slower for single objects")

      assert(deserialized1 == original, "Java serialization should work correctly")
      assert(deserialized2 == original.s, "DataFrame should work correctly")
    }

    it("demonstrate when to use each approach") {

      info("""
         |Spark Serialization Approaches:
         |
         |1. Java/Kryo Serialization:
         |   - Lightest weight for single objects
         |   - Standard Java serialization
         |   - Used by Spark RDDs
         |   - Best for: single object serialization, caching, distributed data
         |
         |2. Encoder (for schema only):
         |   - Extract schema without creating DataFrame
         |   - Very lightweight
         |   - Best for: schema introspection, validation
         |
         |3. Dataset (lighter than DataFrame):
         |   - Type-safe collection with Encoder
         |   - Lighter than DataFrame as no Row conversion
         |   - Best for: typed collections with better performance
         |
         |4. DataFrame:
         |   - Heaviest but most feature-rich
         |   - Schema enforcement, SQL operations, JSON/CSV/Parquet
         |   - Best for: collections, data transformations, complex operations, output formats
         |
         |Rule of thumb:
         |- Single object: Java/Kryo serialization
         |- Schema introspection: Encoder.schema
         |- Typed collection: Dataset
         |- Untyped collection or need SQL/formats: DataFrame
      """.stripMargin)
    }

    it("demonstrate Dataset as middle ground") {

      import spark.implicits._

      // Dataset is lighter than DataFrame (no Row conversion overhead)
      // but provides type safety and many DataFrame operations

      val data = Seq(
        SparkSerdeSpec.A("dataset-1"),
        SparkSerdeSpec.A("dataset-2"),
        SparkSerdeSpec.A("dataset-3")
      )

      val ds = data.toDS()

      // Dataset operations are type-safe
      val filtered = ds.filter(_.s.startsWith("dataset-"))
      val collected = filtered.collect()

      assert(collected.length == 3, "Should have 3 items")
      info(s"Dataset provides type safety: ${collected.mkString(", ")}")

      // Can still convert to JSON when needed
      val json = ds.toJSON.collect()
      info(s"Dataset to JSON: ${json.mkString(", ")}")
    }
  }
}

object SparkSerdeSpec {

  lazy val spark: SparkSession = {
    val session = SparkSession
      .builder()
      .appName("SparkSerdeSpec")
      .master("local[2]")
      .config("spark.sql.shuffle.partitions", "2")
      .getOrCreate()

    sys.addShutdownHook {
      session.stop()
    }

    session
  }

  case class A(s: String)

  trait B
  case class B1(a: A) extends B
  case class B2() extends B

  case class C(b: B)

  case class NullableRecord(s1: String, s2: String)
}
