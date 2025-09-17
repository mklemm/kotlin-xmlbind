pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.github.jacobono") {
                useModule("com.github.jacobono:gradle-jaxb-plugin:${requested.version}")
            }
        }
    }
}

rootProject.name = "kotlin-xmlbind"
include(":kotlin-sourcemodel", ":xmlbuild")
project(":kotlin-sourcemodel").projectDir = File("../kotlin-sourcemodel")
