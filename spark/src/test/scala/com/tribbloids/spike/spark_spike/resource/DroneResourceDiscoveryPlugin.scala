// File: org/example/DroneResourceDiscovery.scala
package com.tribbloids.spike.spark_spike.resource

import org.apache.spark.api.resource.ResourceDiscoveryPlugin
import org.apache.spark.SparkConf
import org.apache.spark.internal.Logging
import org.apache.spark.resource.{ResourceInformation, ResourceRequest}

import java.util.Optional

class DroneResourceDiscoveryPlugin extends ResourceDiscoveryPlugin with Logging {

  override def discoverResource(request: ResourceRequest, sparkConf: SparkConf): Optional[ResourceInformation] = {
    if (request.id.resourceName != "drone") Optional.empty()
    else {
      val addrs = getConnectedDroneAddresses
      logInfo(s"Discovered ${addrs.length} connected drones")
      Optional.of(new ResourceInformation("drone", addrs))
    }
  }

  private def getConnectedDroneAddresses: Array[String] = {
    // Example: Query your drone management system
    // This could be:
    // - USB device enumeration (lsusb on Linux)
    // - Query a local gRPC drone controller service
    // - Check a configuration file with drone IPs
    // For this example, we'll simulate it

    try {
      val process = Runtime.getRuntime.exec(
        Array("bash", "-c", "ls -1 /dev/ttyUSB* 2>/dev/null")
      )
      val reader = scala.io.Source.fromInputStream(process.getInputStream)
      val addrs = reader.getLines().map(_.trim).filter(_.nonEmpty).toArray
      reader.close()
      process.waitFor()
      addrs
    } catch {
      case e: Exception =>
        logWarning(s"Failed to discover drones: ${e.getMessage}")
        Array.empty[String]
    }
  }
}

