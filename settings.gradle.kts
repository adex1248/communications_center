
rootProject.name = "communications_center"



pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    val kotlinVersion: String by settings

    plugins {
        kotlin("plugin.serialization") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    }
}

