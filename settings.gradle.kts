pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
@Suppress("UnstableApiUsage")
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

            // Navigation
            library("navigation-fragment", "androidx.navigation:navigation-fragment-ktx:2.7.4")
            library("navigation-ui", "androidx.navigation:navigation-ui-ktx:2.7.4")

            // Retrofit and Okhttp
            library("retrofit2-retrofit", "com.squareup.retrofit2:retrofit:2.9.0")
            library("retrofit2-converter-gson", "com.squareup.retrofit2:converter-gson:2.9.0")
            library("okhttp3-logging-interceptor", "com.squareup.okhttp3:logging-interceptor:4.9.0")

            // ViewModel and LiveData Lifecycle
            library("lifecycle-viewmodel","androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
            library("lifecycle-livedata","androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

            // Lottie
            library("lottie-animation", "com.airbnb.android:lottie:3.4.0")

            // Paging 3
            library("paging3-runtime","androidx.paging:paging-runtime:3.2.1")
            library("paging3-common","androidx.paging:paging-common:3.2.1")

            // Room
            library("room-runtime","androidx.room:room-runtime:2.5.2")
            library("room-compiler","androidx.room:room-compiler:2.5.2")
            library("room-ktx","androidx.room:room-ktx:2.5.2")
            library("room-paging","androidx.room:room-paging:2.5.2")

            // Datastore
            library("datastore", "androidx.datastore:datastore-preferences:1.0.0")

            // Glide
            library("glide-core", "com.github.bumptech.glide:glide:4.16.0")
            library("glide-ksp", "com.github.bumptech.glide:ksp:4.16.0")

            // CameraX
            library("camerax", "androidx.camera:camera-camera2:1.3.0")
            library("camerax-lifecycle", "androidx.camera:camera-lifecycle:1.3.0")
            library("camerax-view", "androidx.camera:camera-view:1.3.0")
        }
    }
}

rootProject.name = "StoryApp"
include(":app")
 