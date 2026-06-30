plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.foodino"
    buildFeatures.viewBinding=true
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.foodino"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // پوشه‌های تست جداگانه‌ی پروژه (در ریشه‌ی پروژه، کنار app)
    sourceSets {
        getByName("test").java.srcDir("../unit-test")          // یونیت‌تست‌ها
        getByName("androidTest").java.srcDir("../e2e-test")     // تست‌های E2E
    }
}

dependencies {

    implementation(libs.gson)
    implementation(libs.converter.gson)

    //noinspection GradleDependency,GradleDependency,GradleDependency
    implementation(libs.retrofit)

    implementation(libs.converter.scalars)

    //noinspection GradleDependency,GradleDependency
    implementation(libs.com.squareup.okhttp3.okhttp4)
    implementation (libs.sdp.android)

    implementation(libs.github.glide)
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.29")
    //noinspection UseTomlInstead,UseTomlInstead
    implementation ("androidx.cardview:cardview:1.0.0")

    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.uiautomator)
    androidTestImplementation(libs.androidx.test.core)
}