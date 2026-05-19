pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "komposed"
include(":sample")
include(":sample:core:navigation")
include(":sample:core:middleware")
include(":sample:checkout")
include(":sample:checkout:delivery")
include(":sample:checkout:bill")
include(":sample:checkout:placeOrder")
include(":sample:orderDetails")
include(":komposed")
include(":komposed-testing")
