plugins {
    kotlin("jvm") version "2.2.21"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)

}

tasks {
    sourceSets {
        main {
            kotlin.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "9.2.1"
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("tools.aqua:z3-turnkey:4.14.1")
    implementation("org.jgrapht:jgrapht-core:1.5.2")
}
