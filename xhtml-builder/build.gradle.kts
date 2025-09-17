import org.jetbrains.kotlin.config.JvmTarget

buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("org.unbroken-dome.gradle-plugins:gradle-xjc-plugin:2.0.0")
    }
}

plugins {
    kotlin("jvm") version "2.2.20"
//    id("org.unbroken-dome.xjc") version "2.0.0"
//    id("org.hibernate.build.xjc") version "2.2.0"
}

group = "net.codesup.jaxb"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.glassfish.jaxb:jaxb-core:3.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    //xjcClasspath(project(":xmlbuild"))
}

sourceSets.named("main") {
//    xjcExtraArgs.addAll("-extension", "-Xkt")
//    xjcSchema {
//        include("xhtml5.xsd")
//    }
}

System.setProperty("javax.xml.accessExternalSchema", "all") //To solve external schema dependencies

kotlin.sourceSets["main"].kotlin.srcDir(file("build/generated/sources/xkc/kotlin/main"))
kotlin.sourceSets["main"].resources.srcDir(file("build/generated/resources/xkc"))

//xjc {
//    xjcVersion.set("3.0")
//}

//xjc {
//    dependencies {
//        xjc("org.glassfish.jaxb:jaxb-core:3.0.2")
//        xjc("org.glassfish.jaxb:jaxb-xjc:3.0.2")
//        xjc("org.jvnet.jaxb2_commons:jaxb2-basics:0.9.3")
//        xjc("org.jvnet.jaxb2_commons:jaxb2-basics-ant:0.9.3")
//        xjc(project(":jaxb-xkc"))
//    }
//    schemas {
//        create("cfg") {
//            xjcExtensions("kt")
//            xsdFile("src/main/schema/xhtml5.xsd")
//            xjcBindingFile("src/main/schema/binding-config.xjb")
//        }
//    }
//}
