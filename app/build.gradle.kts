plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("dagger.hilt.android.plugin")

    kotlin("kapt")
    kotlin("plugin.parcelize")
}

android {
    namespace = "com.hnu.heshequ"

    defaultConfig {
        applicationId = "com.hnu.heshequ"

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
}

dependencies {
    implementation(project(":network"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.viewpager2)

    implementation(libs.splitties.appctx)

    implementation(libs.banner)
    implementation(libs.status.bar.compat)

    // 支持 GIF 动图、WebP（静态图 + 动图），需要添加
    implementation(libs.facebook.fresco)
    implementation(libs.facebook.fresco.gif)
    implementation(libs.facebook.fresco.webp)
    implementation(libs.facebook.fresco.websupport)

    implementation(libs.androidx.multidex)

    //滚度刻度尺
    implementation(libs.zkkrulerview)

    implementation(libs.fastjson)

    implementation(libs.xrecyclerview)
    implementation(libs.luban)
    implementation(libs.rollviewpager)
    implementation(libs.photoview)
    implementation(libs.eventbus)

    // 生成二维码依赖
    implementation(libs.zxing.library)

    // 布兰柯基工具类
    implementation(libs.utilcode)

    implementation(libs.okhttputils)
    implementation(libs.okhttpserver)

    // 小三角 需要 Maven https://jitpack.io
    implementation(libs.sharpview)

    // 时间选择器
    implementation(libs.android.pickerview)

    implementation(libs.jsoup)

    implementation("com.github.huzhenjie:RichTextEditor:1.0.4") {
        exclude(group = "com.github.bumptech.glide")
    }

    implementation(libs.glide)
    annotationProcessor(libs.glide)


    // XRefreshView
    implementation(libs.xrefreshview)

    // 加载中
    implementation(libs.android.promptdialog)

    implementation(libs.gson)
    implementation(libs.okhttp3) {
        exclude(group = "glide-parent")
    }
    implementation(libs.okhttp3.logging.interceptor) {
        exclude(group = "glide-parent")
    }

    implementation(libs.retrofit)
    implementation(libs.retrofit.adapter.rxjava)
    implementation(libs.retrofit.converter.gson)
//    implementation("io.reactivex:rxandroid:1.2.1")
//    implementation(libs.rxjava)

    implementation(libs.androidx.swiperefreshlayout)

    implementation(libs.mavericks)
    implementation(libs.hilt.android)
    kapt(libs.androidx.hilt.compiler)
    kapt(libs.hilt.android.compiler)

//    implementation(libs.crashreport) //其中latest.release指代最新Bugly SDK版本号，也可以指定明确的版本号，例如4.0.3

    testImplementation(libs.junit)

}