plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id ("kotlin-android")
    //id("com.google.dagger.hilt.android")
    //id("dagger.hilt.android.plugin")
}

android {
    namespace = "mo.zain.marassi"
    compileSdk = 32

    defaultConfig {
        applicationId = "mo.zain.marassi"
        minSdk = 24
        targetSdk = 32
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
    kapt{generateStubs=true}
}

dependencies {

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")

    //Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    //Coroutines Lifecycle
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    //coil
    implementation("io.coil-kt:coil:2.1.0")

    //sdp
    implementation ("com.intuit.sdp:sdp-android:1.1.0")

    //Rounded
    implementation ("com.makeramen:roundedimageview:2.3.0")

    //Loading Of Image
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.2")



//    implementation ("com.google.dagger:hilt-android:2.40")
//    //implementation ("androidx.hilt:hilt-work:1.0.0")
//    kapt ("com.google.dagger:hilt-android-compiler:2.38.1")
//    kapt ("androidx.hilt:hilt-compiler:1.0.0")
//    implementation ("androidx.activity:activity-ktx:1.5.1")
    //implementation ("androidx.hilt:hilt-navigation-fragment:1.0.0")

    implementation ("com.airbnb.android:lottie:4.1.0")


}
//hilt {
//    enableAggregatingTask = true
//}
//kapt {
//    correctErrorTypes = true
//}