object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.1.0"
    const val kotlinResult = "com.michael-bull.kotlin-result:kotlin-result:1.1.9"
    const val ktlintGradlePlugin = "org.jlleitschuh.gradle:ktlint-gradle:9.4.0"

    const val bintrayPlugin = "com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4"
    const val androidMavenPlugin = "com.github.dcendents:android-maven-gradle-plugin:2.1"

    const val moshiLazyAdapters = "com.serjltt.moshi:moshi-lazy-adapters:2.2"


    const val junit = "junit:junit:4.13"
    const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
    const val mockitoAndroid = "org.mockito:mockito-android:3.8.0"

    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0"

    object Kotlin {
        private const val version = "1.4.0"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.3.9"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.3.0-alpha02"
        const val activity = "androidx.activity:activity-ktx:1.2.0-alpha08"
        const val coreKtx = "androidx.core:core-ktx:1.5.0-alpha02"
        const val webkit = "androidx.webkit:webkit:1.2.0"

        object Lifecycle {
            private const val version = "2.2.0"
            const val common = "androidx.lifecycle:lifecycle-common-java8:$version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }

        object Test {
            private const val version = "1.2.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
        }
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val moshiConverter = "com.squareup.retrofit2:converter-moshi:$version"
    }

    object Moshi {
        private const val version = "1.10.0"
        const val moshi = "com.squareup.moshi:moshi-kotlin:$version"
        const val moshiAdapters = "com.squareup.moshi:moshi-adapters:$version"
        const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
    }

    object OkHttp {
        private const val version = "4.8.0"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }
}