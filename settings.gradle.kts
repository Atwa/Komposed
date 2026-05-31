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
include(":app")
include(":app:core:navigation")
include(":app:core:middleware")
include(":app:checkout")
include(":app:checkout:delivery")
include(":app:checkout:bill")
include(":app:checkout:placeOrder")
include(":app:orderDetails")
include(":komposed")
include(":komposed-testing")
