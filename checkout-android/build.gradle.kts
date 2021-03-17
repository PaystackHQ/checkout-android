plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "29.0.3"

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
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
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    lintOptions {
        enable("Interoperability")
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

// Avoid Kotlin docs error
tasks.withType<Javadoc>().all {
    enabled = false
}

dependencies {
    implementation(Libs.Kotlin.stdlib)

    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.activity)
    implementation(Libs.AndroidX.webkit)
    implementation(Libs.AndroidX.Lifecycle.common)
    implementation(Libs.AndroidX.Lifecycle.livedata)

    implementation(Libs.constraintLayout)

    implementation(Libs.Retrofit.retrofit)
    implementation(Libs.Retrofit.moshiConverter)
    implementation(Libs.OkHttp.okhttp)
    implementation(Libs.OkHttp.loggingInterceptor)

    implementation(Libs.kotlinResult)

    implementation(Libs.Moshi.moshi)
    implementation(Libs.Moshi.moshiAdapters)
    implementation(Libs.moshiLazyAdapters)
    kapt(Libs.Moshi.moshiCodegen)

    testImplementation(Libs.junit)
    testImplementation(Libs.mockitoKotlin)
    testImplementation(Libs.Coroutines.test)

    androidTestImplementation(Libs.mockitoAndroid)
    androidTestImplementation(Libs.AndroidX.Test.core)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
}

extra.apply {
    set("libraryVersion", "1.0.0-beta01")

    set("bintrayRepo", "maven")
    set("bintrayName", "checkout-android")

    set("publishedGroupId", "com.paystack.checkout")
    set("libraryName", "checkout-android")     // this is the module name of library
    set("artifact", "checkout-android")

    set("libraryDescription", "Paystack Checkout SDK for Android")

    set("siteUrl", "https://github.com/paystackhq/checkout-android")
    set("gitUrl", "https://github.com/paystackhq/checkout-android.git")


    set("developerId", "paystack")
    set("developerName", "Paystack")
    set("developerEmail", "developers@paystack.co")

    set("licenseName", "Apache License, Version 2.0")
    set("licenseUrl", "https://opensource.org/licenses/Apache-2.0")
    set("allLicenses", arrayOf("Apache-2.0"))
}

apply(from = "publish.gradle")
