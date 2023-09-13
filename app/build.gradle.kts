plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = 34
    namespace = "com.laelar.app"

    defaultConfig {
        applicationId = "com.laelar.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    //android(ktx
    implementation("androidx.core:core-ktx:1.12.0")

    //app compat
    implementation("androidx.appcompat:appcompat:1.6.1")

    //preference
    implementation("androidx.preference:preference-ktx:1.2.1")

    //material
    implementation("com.google.android.material:material:1.9.0")

    //constraint
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    //navigation available 2.7.0
    val navVersion = "2.6.0"
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    //lifecycle
    val lifecycleVersion = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    //dagger hilt
    val hiltVersion = "2.45"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    //glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    //datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //splashscreen
    implementation("androidx.core:core-splashscreen:1.0.1")

    //anhance
    implementation("com.github.cinkhangin:anhance:1.3.1")
    implementation("com.github.cinkhangin:firex:1.2.0")
    implementation("com.github.cinkhangin:glow:0.2.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    //room
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")


    implementation(
        fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))
    )
    implementation(project(":core"))
}

// Allow references to generated code
kapt { correctErrorTypes = true }
