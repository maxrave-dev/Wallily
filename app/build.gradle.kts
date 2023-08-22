plugins {
    id("com.android.application")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.maxrave.wallily"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.maxrave.wallily"
        minSdk = 26
        targetSdk = 33
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.retrofit2:adapter-rxjava3:$retrofit_version")
    val okhttp_version = "5.0.0-alpha.10"
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    implementation("io.coil-kt:coil:2.4.0")

    implementation("com.squareup.picasso:picasso:2.8")
    //Easy Permissions
    implementation("pub.devrel:easypermissions:3.0.0")
    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    //Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    annotationProcessor("com.google.dagger:hilt-compiler:2.47")
    //Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //Lottie
    val lottieVersion = "6.1.0"
    implementation("com.airbnb.android:lottie:$lottieVersion")

    //Paging 3
    val paging_version= "3.2.0"
    implementation("androidx.paging:paging-runtime:$paging_version")
    // optional - RxJava3 support
    implementation("androidx.paging:paging-rxjava3:$paging_version")

    val autoDispose = "2.2.1"
    implementation("com.uber.autodispose2:autodispose:$autoDispose")
    implementation("com.uber.autodispose2:autodispose-android:$autoDispose")
    implementation("com.uber.autodispose2:autodispose-androidx-lifecycle:$autoDispose")
    implementation("com.uber.autodispose2:autodispose-lifecycle:$autoDispose")

}