plugins {
    kotlin("jvm") version "2.1.0"
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
        gradleVersion = "8.3"
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    implementation("org.jgrapht:jgrapht-core:1.5.2")
}
