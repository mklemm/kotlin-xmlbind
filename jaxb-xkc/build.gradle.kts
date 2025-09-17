buildscript {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    kotlin("jvm") version ("2.2.10")
    //id("myproject.kotlin-conventions")
}

group = "net.codesup.jaxb"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(project(":kotlin-sourcemodel"))
    implementation("org.glassfish.jaxb:xsom:4.0.5")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "19"
    }
}
