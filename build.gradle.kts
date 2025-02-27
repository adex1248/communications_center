plugins {
    kotlin("jvm") version "2.1.10"
    `java-library`
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    //maven { url = URL("https://mvnrepository.com/artifact/org.apache.commons/commons-lang3") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("commons-cli:commons-cli:1.8.0")
    //implementation("org.apache.commons:commons-math3:3.6.1")
    //implementation("org.apache.commons:commons-lang3:3.15.0")
    //implementation("org.apache.commons:commons-collections4:4.4")
    implementation("com.illposed.osc:javaosc-core:0.8")
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