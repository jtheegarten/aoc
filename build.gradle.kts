plugins {
    kotlin("multiplatform") version "1.7.22"
}

repositories {
    mavenCentral()
}

kotlin {
    mingwX64("native") {
        binaries {
            executable {
                entryPoint = "net.sheltem.aoc.y2022.main"
            }
        }
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "7.6"
    distributionType = Wrapper.DistributionType.BIN
}
