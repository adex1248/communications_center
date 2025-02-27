import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization")
    `java-library`
}
application {
    mainClass.set("dev.slimevr.Main")
}
tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.set(listOf("-Xvalue-classes"))
    }
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("commons-cli:commons-cli:1.8.0")
    //implementation("org.apache.commons:commons-math3:3.6.1")
    //implementation("org.apache.commons:commons-lang3:3.15.0")
    //implementation("org.apache.commons:commons-collections4:4.4")
    //implementation("org.apache.commons:commons-math3:3.6.1")
    //implementation("com.melloware:jintellitype:1.+")
    implementation("com.illposed.osc:javaosc-core:0.8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")


    //This is not used in SlimeVR, but IDK how they got around it
    implementation("org.jmonkeyengine:jme3-core:3.4.0-stable")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}