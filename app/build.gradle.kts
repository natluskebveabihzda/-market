plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.jetbrainsKotlinAndroid)
  alias(libs.plugins.daggerHilt)
  alias(libs.plugins.androidXNavigationSaveArgs)
  kotlin("kapt")
}

android {
  namespace = "com.deveem.dmarket"
  compileSdk = 34
  
  defaultConfig {
    applicationId = "com.deveem.dmarket"
    minSdk = 28
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"
    
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    
    renderscriptTargetApi = 19
    renderscriptSupportModeEnabled = true
    
    kapt {
      arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
      }
    }
  }
  
  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    viewBinding = true
    buildConfig = true
  }
}

dependencies {
  // Core
  implementation(libs.androidx.core.ktx)
  
  // Architecture components
  implementation(libs.androidx.activity)
  implementation(libs.androidx.fragment)
  implementation(libs.lifecycle.livedata.ktx)
  implementation(libs.lifecycle.viewmodel.ktx)
  implementation(libs.lifecycle.runtime.ktx)
  implementation(libs.lifecycle.common)
  
  // UI
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.viewpager2)
  implementation(libs.dotsindicator)
  
  // Navigation
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)
  
  // Hilt
  implementation(libs.dagger.hilt.android)
  kapt(libs.dagger.hilt.compiler)
  
  // Retrofit & OkHttp
  implementation(libs.retrofit2)
  implementation(libs.retrofit2.converter.gson)
  implementation(libs.okhttp3)
  implementation(libs.gson)
  
  // Coil
  implementation(libs.coil)
  
  // Testings
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  
  implementation("com.google.android.gms:play-services-auth:21.1.1")
  
  implementation ("androidx.credentials:credentials:1.2.2")
  implementation ("androidx.security:security-identity-credential:1.0.0-alpha03")
  
  implementation ("androidx.credentials:credentials-play-services-auth:1.2.2")
  implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.0")
  
  // Room
  implementation ("androidx.room:room-ktx:2.6.1")
  kapt ("androidx.room:room-compiler:2.6.1")
  
  // Blur View
  implementation ("com.github.Dimezis:BlurView:version-2.0.3")
  
  implementation(libs.logging.interceptor) {
    exclude(group = "org.json", module = "json")
  }
  // Paging 3
  implementation(libs.paging.paging)
  implementation(libs.paging.common)
  
  
}