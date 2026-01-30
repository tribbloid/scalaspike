package com.tribbloids.spike.spark_spike.resource

import ai.acyclic.prover.commons.spark.TestHelper
import org.apache.spark.resource.{ResourceProfile, ResourceProfileBuilder, TaskResourceRequests}
import org.scalatest.funspec.AnyFunSpec

class TaskResourceRequestsSpike extends AnyFunSpec {

  def expensiveGpuFunc(num: Int): Int = {
    // meaningful GPU work would happen here
    num + 1
  }
  it("example") {

    val spark = TestHelper.TestSparkSession
    val sc = spark.sparkContext

    // 1. Define a standard RDD (uses default cluster resources, e.g., CPU only)
    val data = sc.parallelize(1 to 1000)

    // 2. Define a "Heavy" Profile (e.g., requires 1 GPU per task)
    //    We create a request for resources and build the profile.
    val gpuReqs: TaskResourceRequests = new TaskResourceRequests().resource("gpu", 1.0)
    val gpuProfile: ResourceProfile = new ResourceProfileBuilder().require(gpuReqs).build()

    // 3. Apply the profile to a specific operation
    //    The .withResources() call forces a new stage that will only
    //    schedule tasks on executors matching these requirements.
    val heavyResult = data
      .map(x => x * 2) // Runs on default resources
      .withResources(gpuProfile) // SWITCH: Next operations need the GPU profile
      .map(x => expensiveGpuFunc(x)) // Runs on GPU-equipped executors
      .collect()

  }

}
