
import org.gradle.api.Project

import org.gradle.kotlin.dsl.*

/**
 * Configures the current project as a Kotlin project by adding the Kotlin `stdlib` as a dependency.
 */
fun Project.spikeExt(): SpikeExt {

    return SpikeExt(this)
}

class SpikeExt(self: Project){

    val scalaV: String = self.properties.get("scalaVersion").toString()
    val scalaBinaryV: String = scalaV.split('.').subList(0,2).joinToString(".")

    val sparkV = self.properties.get("sparkVersion")
}