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
    versionCatalogs {
        create("libs") {
            // Built-in
            library("androidx-core", "androidx.core:core-ktx:1.12.0")
            library("androidx-appCompat", "androidx.appcompat:appcompat:1.6.1")
            library("google-material", "com.google.android.material:material:1.10.0")
            library("androidx-constraintLayout", "androidx.constraintlayout:constraintlayout:2.1.4")
            library("test-junit", "junit:junit:4.13.2")
            library("androidTest-junit", "androidx.test.ext:junit:1.1.5")
            library("androidTest-espresso", "androidx.test.espresso:espresso-core:3.5.1")

            // Splash Screen
            library("androidx-splashscreen", "androidx.core:core-splashscreen:1.0.0")

            // Dagger Hilt
            library("dagger-hilt-core","com.google.dagger:hilt-android:2.48")
            library("dagger-hilt-compiler","com.google.dagger:hilt-android-compiler:2.48")
        }
    }
}

rootProject.name = "StoryApp"
include(":app")
 