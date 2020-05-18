import org.gradle.api.Project

class Versions(self: Project) {

    val scalaV: String = self.properties.get("scalaVersion").toString()

    protected val scalaVparts = scalaV.split('.')

    val scalaBinaryV: String = scalaVparts.subList(0, 2).joinToString(".")
    val scalaMinorV: String = scalaVparts[2]

    val sparkV: String = self.properties.get("sparkVersion").toString()

}
