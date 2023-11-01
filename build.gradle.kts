buildscript {
    repositories {
        // Add here whatever repositories you're already using
        mavenCentral()
    }
}

plugins {
    id("ai.acyclic.scala2-conventions")
    id("ai.acyclic.publish-conventions")
}


idea {

    module {
        excludeDirs.add(file("prover-commons"))

        // apache spark
        excludeDirs.add(file("warehouse"))

        excludeDirs.add(file("latex"))
    }
}