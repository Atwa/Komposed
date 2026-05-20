plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "io.github.atwa.komposed.testing"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    api(project(":komposed"))
    implementation(libs.kotlinx.coroutines.test)
}

// ─── Publishing ────────────────────────────────────────────────────────────────

afterEvaluate {
    val versionName = project.findProperty("VERSION_NAME") as? String ?: "1.0.0"

    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId    = "io.github.atwa"
                artifactId = "komposed-testing"
                version    = versionName

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
        }

        repositories {
            maven {
                name = "MavenCentral"
                url = if (versionName.endsWith("SNAPSHOT"))
                    uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                else
                    uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.findProperty("OSSRH_USERNAME") as? String
                        ?: System.getenv("OSSRH_USERNAME")
                    password = project.findProperty("OSSRH_PASSWORD") as? String
                        ?: System.getenv("OSSRH_PASSWORD")
                }
            }
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
            sign(publishing.publications["release"])
        }
    }
}
