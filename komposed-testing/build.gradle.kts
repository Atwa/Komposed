plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    id("maven-publish")
    id("signing")
}

kotlin {
    androidTarget {
        compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) }
    }
    jvm {
        compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":komposed"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "io.github.atwa.komposed.testing"
    compileSdk {
        version = release(36)
    }
    defaultConfig {
        minSdk = 24
    }
}

// ─── Publishing ────────────────────────────────────────────────────────────────

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

afterEvaluate {
    val versionName = project.findProperty("VERSION_NAME") as? String ?: "1.0.0"

    publishing {
        publications.withType<MavenPublication>().configureEach {
            groupId = "io.github.atwa"
            version = versionName

            if (name == "jvm") {
                artifact(javadocJar)
            }

            pom {
                name.set("Komposed Testing")
                description.set(
                    "Testing utilities for Komposed — TestStore, ReduceResult DSL, " +
                    "TestNavigator, and SpyMiddleware for unit and integration testing " +
                    "of Komposed stores and reducers."
                )
                url.set("https://github.com/atwa/komposed")
                inceptionYear.set("2024")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("atwa")
                        name.set("Ahmed Atwa")
                        email.set("a.atwaa94@gmail.com")
                        url.set("https://github.com/atwa")
                    }
                }
                scm {
                    url.set("https://github.com/atwa/komposed")
                    connection.set("scm:git:git://github.com/atwa/komposed.git")
                    developerConnection.set("scm:git:ssh://git@github.com/atwa/komposed.git")
                }
            }
        }

        repositories {
            maven {
                name = "MavenLocal"
                url = uri("${rootDir}/build/local-publish")
            }
        }
    }

    signing {
        val signingKey = project.findProperty("GPG_PRIVATE_KEY") as? String
            ?: System.getenv("GPG_PRIVATE_KEY")
        val signingPassword = project.findProperty("GPG_KEY_PASSWORD") as? String
            ?: System.getenv("GPG_KEY_PASSWORD")
        if (signingKey != null) {
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publishing.publications)
        }
    }
}
