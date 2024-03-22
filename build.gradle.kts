import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}

buildscript {
    dependencies {
        classpath(libs.bundles.gradle.plugins)
        classpath(libs.commons.codec)
    }
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
            freeCompilerArgs.addAll("-Xjvm-default=all", "-opt-in=kotlin.RequiresOptIn")
        }
    }

    afterEvaluate {
        if (plugins.hasPlugin("com.android.library") ||
            plugins.hasPlugin("com.android.application")
        ) {
            extensions.getByName<BaseExtension>("android").apply {
                compileSdkVersion(34)

                defaultConfig {
                    if (minSdk == null) {
                        minSdk = 27
                    }
                    if (plugins.hasPlugin("com.android.application")) {
                        targetSdk = 34
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }
            }
        }
    }
}
