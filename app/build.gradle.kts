plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.hnu.heshequ"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hnu.heshequ"
        minSdk = 27
        targetSdk = 34
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    implementation("com.youth.banner:banner:1.4.10")
    implementation("com.githang:status-bar-compat:0.5")

    // 支持 GIF 动图、WebP（静态图 + 动图），需要添加
    implementation("com.facebook.fresco:fresco:0.12.0")
    implementation("com.facebook.fresco:animated-gif:0.12.0")
    implementation("com.facebook.fresco:animated-webp:0.12.0")
    implementation("com.facebook.fresco:webpsupport:0.12.0")

    implementation("androidx.multidex:multidex:2.0.1")

    //滚度刻度尺
    implementation("com.zkk.view:ZkkRulerView:1.0.0")

    implementation("com.alibaba:fastjson:1.2.79")

    implementation("com.jcodecraeer:xrecyclerview:1.6.0")
    implementation("top.zibin:Luban:1.1.2")
    implementation("com.jude:rollviewpager:1.2.9")
    implementation("com.github.chrisbanes:PhotoView:2.0.0")
    implementation("org.greenrobot:eventbus:3.1.1")

    // 生成二维码依赖
    implementation("cn.yipianfengye.android:zxing-library:2.1")

    // 布兰柯基工具类
    implementation("com.blankj:utilcode:1.24.2")

    implementation("com.lzy.net:okhttputils:1.8.1")
    implementation("com.lzy.net:okhttpserver:1.0.3")

    // 小三角 需要 Maven https://jitpack.io
    implementation("com.github.zengzhaoxing:SharpView:v2.3.0")

    // 时间选择器
    implementation("com.contrarywind:Android-PickerView:4.1.4")

    implementation("org.jsoup:jsoup:1.11.2")

    implementation("com.github.huzhenjie:RichTextEditor:1.0.4") {
        exclude(group = "com.github.bumptech.glide")
    }

    implementation("com.github.bumptech.glide:glide:3.7.0")

    // XRefreshView
    implementation("com.huxq17.xrefreshview:xrefreshview:3.6.9")

    // 加载中
    implementation("com.github.limxing:Android-PromptDialog:1.1.3")

    implementation("com.google.code.gson:gson:2.8.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.3.0")
    implementation("com.squareup.retrofit2:adapter-rxjava:2.1.0")
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation("io.reactivex:rxandroid:1.2.1")
    implementation("io.reactivex:rxjava:1.2.1")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("com.tencent.bugly:crashreport:latest.release") //其中latest.release指代最新Bugly SDK版本号，也可以指定明确的版本号，例如4.0.3

}