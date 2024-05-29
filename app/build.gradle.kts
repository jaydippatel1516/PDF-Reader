plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "pdf.reader.pdfviewer"
    compileSdk = 34

    defaultConfig {
        applicationId = "pdf.reader.pdfviewer"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures {
//        viewBinding = true //for viewbinding
        dataBinding = true //for dataBinding
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.intuit.ssp:ssp-android:1.1.0")
    implementation ("com.github.bumptech.glide:glide:4.15.0")
    implementation ("com.airbnb.android:lottie:6.2.0")
    implementation ("com.github.thekhaeng:pushdown-anim-click:1.1.1")
    implementation ("com.karumi:dexter:6.2.3")
    implementation ("com.roughike:bottom-bar:2.3.1")
    implementation ("com.github.clans:fab:1.6.4")
    implementation ("com.github.lzyzsd:circleprogress:1.2.4")
    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    implementation ("com.theartofdev.edmodo:android-image-cropper:2.8.0")
    implementation ("com.akexorcist:round-corner-progress-bar:2.2.1")
    implementation ("com.github.ybq:Android-SpinKit:1.4.0")
    implementation ("com.davemorrissey.labs:subsampling-scale-image-view:3.10.0")
    implementation ("pub.devrel:easypermissions:3.0.0")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.0")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.10")
    implementation ("com.tom-roush:pdfbox-android:2.0.27.0")
    implementation ("com.itextpdf:itextpdf:5.5.13.3")
    implementation(libs.firebase.crashlytics.buildtools)
    implementation ("com.google.code.gson:gson:2.11.0")
    implementation(libs.core.ktx)


}