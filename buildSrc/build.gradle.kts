apply {
    plugin("kotlin")
}

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", "1.9.0"))
    }
}

repositories {
    gradlePluginPortal()
}