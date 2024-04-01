import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("dagger.hilt.android.plugin")

    kotlin("kapt")
    kotlin("plugin.parcelize")
}

android {
    namespace = "com.leslie.socialink"

    defaultConfig {
        applicationId = "com.leslie.socialink"

        /**
         * versionName 和 versionCode 是 Android 应用程序的版本管理中常用的两个属性，它们有以下区别：
         *
         * versionName: versionName 是用来展示给用户的版本号，通常是一个易于理解的字符串，例如 "1.0"、"v2.1.3" 等。这个版本号用于标识应用程序的发布版本，用户可以通过这个版本号了解应用程序的更新情况。
         * versionCode: versionCode 是一个用于内部管理的整数值，它代表了应用程序的版本号，用于在代码中判断应用程序的版本顺序。每次发布新版本时，versionCode 都应该递增，这样可以确保新版本的 versionCode 大于旧版本，方便系统判断版本更新。
         *
         * 总结起来，versionName 是给用户看的，用于展示版本号的易读字符串；而 versionCode 是给开发者和系统看的，用于在代码中处理版本号的整数值。
         */
        versionCode = 1
        versionName = "v1.0.0"

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

    applicationVariants.all {
        outputs.map { it as com.android.build.gradle.internal.api.ApkVariantOutputImpl }
            .all { output ->
                output.outputFileName = "Socialink-$versionName-${currentTime()}.apk"
                false
            }
    }

}

fun currentTime(): String {
    val currentTime = Date()
    val sdf = SimpleDateFormat("yyyyMMdd-HHmmss") // 自定义时间格式
    return sdf.format(currentTime)
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