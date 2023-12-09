plugins {
    kotlin("jvm") version "1.9.21"
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
        gradleVersion = "7.6"
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
}
