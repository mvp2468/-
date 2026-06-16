pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { setUrl("https://jitpack.io") }
    }
}

// =========================================================================
// Local Build Cache - caches compilation & task outputs between builds
// Already enabled via org.gradle.caching=true in gradle.properties
// =========================================================================
buildCache {
    local {
        isEnabled = true
        directory = file("${rootDir}/.gradle/build-cache")
    }
}

include(":app")
include(":analytics:testkitchen")
