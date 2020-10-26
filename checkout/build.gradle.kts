import com.github.panpf.bintray.publish.PublishExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
    id("com.github.panpf.bintray-publish")
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

    androidTestImplementation(Libs.mockitoKotlin)
    androidTestImplementation(Libs.mockitoAndroid)
    androidTestImplementation(Libs.AndroidX.Test.core)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
}

configure<PublishExtension> {
    userOrg = "paystack"
    groupId = "com.paystack"
    artifactId = "checkout-android"
    publishVersion = "0.1.0-alpha"
    desc = "Paystack Checkout SDK for Android"
    website = "https://github.com/PaystackHQ/checkout-android"

    val prop = Properties()
    val fis = FileInputStream(project.rootProject.file("local.properties"))
    prop.load(fis)

    bintrayUser = prop.getProperty("bintray.user")
    bintrayKey = prop.getProperty("bintray.apikey")
    dryRun = false
}
