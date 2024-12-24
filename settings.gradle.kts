enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
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
//    versionCatalogs {
//        create("libs") {
//            from(files("gradle/lib.versions.toml"))
//        }
//    }
}

rootProject.name = "Assignment1"
include(":app")
include(":core:textvalidation")
include(":core:datastore")
include(":core:contactsprovider")
include(":core:webapi")
