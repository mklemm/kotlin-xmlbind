import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.20"
}

group = "com.kscs"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(project(":kotlin-sourcemodel"))
    implementation("org.glassfish.jaxb:xsom:4.0.5")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(24)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
