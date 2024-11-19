plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
}


android {
    namespace = "com.alexP.socialnetwork"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.alexP.socialnetwork"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(projects.core.datastore)
    implementation(projects.core.contactsprovider)
    implementation(projects.core.textvalidation)


    // AndroidX Core KTX for Kotlin extensions
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity.ktx)

    // AndroidX AppCompat for backward-compatible UI features
    implementation(libs.androidx.appcompat)

    // Material Design components for modern UI
    implementation(libs.material)

    // ConstraintLayout for flexible and responsive UI
    implementation(libs.constraintLayout)

    // Glide for efficient image loading and caching
    implementation(libs.glide)

    // Testing dependencies

    // JUnit for unit testing
    testImplementation(libs.junit)

    // AndroidX Test extensions for JUnit
    androidTestImplementation(libs.testExtJUnit)

    // Espresso for UI testing
    androidTestImplementation(libs.espressoCore)

    // Kotlin Coroutines for asynchronous programming
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // AndroidX Lifecycle components for managing UI-related data
    implementation(libs.lifecycleRuntime)
    implementation(libs.lifecycleViewmodel)
}