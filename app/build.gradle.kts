plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myrssreaderapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.myrssreaderapp"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    // Retrofit + SimpleXML
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-simplexml:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // Picasso
    implementation("com.squareup.picasso:picasso:2.8")
    // JSoup
    implementation("org.jsoup:jsoup:1.16.1")
    // gson Day 7
    implementation("com.google.code.gson:gson:2.11.0")
}