plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrainsKotlin)
}

android {
    namespace = "com.alexp.datastore"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // DataStore for data storage using Kotlin Coroutines
    implementation(libs.androidx.datastorePreferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.testExtJUnit)
    androidTestImplementation(libs.espressoCore)
}