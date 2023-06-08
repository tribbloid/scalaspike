import org.gradle.api.Project

class Versions(private val self: Project) {

    inner class Scala {
        val group: String = self.properties["scala.group"].toString()

        val v: String = self.properties["scala.version"].toString()
        protected val vParts: List<String> = v.split('.')

        val binaryV: String = vParts.subList(0, 2).joinToString(".")
        val minorV: String = vParts[2]
    }
    val scala = Scala()

    val sparkV: String = self.properties.get("spark.version").toString()

    val scalaTestV: String = "3.2.16"

    val splainV: String = self.properties.get("splain.version")?.toString() ?: ""
}
