# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**  { *; }

-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.**  { *; }

-keep class javax.servlet.** { *; }

-keep class java.lang.reflect.** { *; }
-keep class * extends java.lang.reflect.ParameterizedType { *; }

 # Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

 -keep class com.leslie.socialink.bean.** { *; }
 -keep class com.leslie.socialink.entity.** { *; }
 -keep class com.leslie.socialink.network.entity.** { *; }


# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 关闭混淆
#-dontobfuscate

# keep source file and line number table, so log can be more helpful.
-keepattributes SourceFile,LineNumberTable
-keepattributes Exceptions

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**
# These classes are duplicated between android.jar and org.apache.http.legacy.jar.
-dontnote android.net.http.*
-dontnote org.apache.http.**

-dontwarn android.**

-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

-dontnote org.apache.commons.codec.**

-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}


##---------------Begin: proguard configuration for notes and warns  ----------
-dontnote com.fasterxml.jackson.**
-dontnote org.conscrypt.**
-dontnote io.netty.**
-dontnote com.badlogic.**
-dontnote android.support.**
##---------------End: proguard configuration for notes and warns  ----------


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

# Gson specific classes
-dontnote sun.misc.Unsafe
-dontwarn sun.misc.**
-dontnote io.netty.channel.**
-dontnote com.facebook.sonar.**
-dontwarn com.facebook.**
-keep class sun.misc.Unsafe { *; }
-dontwarn org.mozilla.javascript.**
##---------------End: proguard configuration for Gson  ----------


##---------------Begin: proguard configuration for miot sdk  ----------
-dontwarn io.netty.**
-dontnote com.inuker.bluetooth.library.**
##---------------End: proguard configuration for miot sdk  ----------


##---------------Begin: proguard configuration for milink  ----------
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontnote com.google.protobuf.**
#-dontnote javax.servlet.**
##---------------End: proguard configuration for milink  ----------


##---------------Begin: proguard configuration for Guava (https://github.com/google/guava/wiki/UsingProGuardWithGuava)   ----------
-dontwarn javax.lang.model.element.Modifier

# Note: We intentionally don't add the flags we'd need to make Enums work.
# That's because the Proguard configuration required to make it work on
# optimized code would preclude lots of optimization, like converting enums
# into ints.

# Throwables uses internal APIs for lazy stack trace resolution
-dontnote sun.misc.SharedSecrets
-keep class sun.misc.SharedSecrets {
  *** getJavaLangAccess(...);
}
-dontnote sun.misc.JavaLangAccess
-keep class sun.misc.JavaLangAccess {
  *** getStackTraceElement(...);
  *** getStackTraceDepth(...);
}

# FinalizableReferenceQueue calls this reflectively
# Proguard is intelligent enough to spot the use of reflection onto this, so we
# only need to keep the names, and allow it to be stripped out if
# FinalizableReferenceQueue is unused.
-keepnames class com.google.common.base.internal.Finalizer {
  *** startFinalizer(...);
}
# However, it cannot "spot" that this method needs to be kept IF the class is.
-keepclassmembers class com.google.common.base.internal.Finalizer {
  *** startFinalizer(...);
}
-keepnames class com.google.common.base.FinalizableReference {
  void finalizeReferent();
}
-keepclassmembers class com.google.common.base.FinalizableReference {
  void finalizeReferent();
}

# Striped64, LittleEndianByteArray, UnsignedBytes, AbstractFuture
-dontwarn sun.misc.Unsafe

# Striped64 appears to make some assumptions about object layout that
# really might not be safe. This should be investigated.
-keepclassmembers class com.google.common.cache.Striped64 {
  *** base;
  *** busy;
}
-keepclassmembers class com.google.common.cache.Striped64$Cell {
  <fields>;
}

-dontwarn java.lang.SafeVarargs

-keep class java.lang.Throwable {
  *** addSuppressed(...);
}

# Futures.getChecked, in both of its variants, is incompatible with proguard.

# Used by AtomicReferenceFieldUpdater and sun.misc.Unsafe
-keepclassmembers class com.google.common.util.concurrent.AbstractFuture** {
  *** waiters;
  *** value;
  *** listeners;
  *** thread;
  *** next;
}
-keepclassmembers class com.google.common.util.concurrent.AtomicDouble {
  *** value;
}
-keepclassmembers class com.google.common.util.concurrent.AggregateFutureState {
  *** remaining;
  *** seenExceptions;
}

# Since Unsafe is using the field offsets of these inner classes, we don't want
# to have class merging or similar tricks applied to these classes and their
# fields. It's safe to allow obfuscation, since the by-name references are
# already preserved in the -keep statement above.
-keep,allowshrinking,allowobfuscation class com.google.common.util.concurrent.AbstractFuture** {
  <fields>;
}

# Futures.getChecked (which often won't work with Proguard anyway) uses this. It
# has a fallback, but again, don't use Futures.getChecked on Android regardless.
-dontwarn java.lang.ClassValue

# MoreExecutors references AppEngine
-dontnote com.google.appengine.api.ThreadManager
-keep class com.google.appengine.api.ThreadManager {
  static *** currentRequestThreadFactory(...);
}
-dontnote com.google.apphosting.api.ApiProxy
-keep class com.google.apphosting.api.ApiProxy {
  static *** getCurrentEnvironment (...);
}
##---------------End: proguard configuration for Guava  ----------


##---------------Begin: proguard configuration for lottie  ----------
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** {*;}
##---------------End: proguard configuration for lottie  ----------


##---------------Begin: kotlinx.serialization----------
# Keep `Companion` object fields of serializable classes.
# This avoids serializer lookup through `getDeclaredClasses` as done for named companion objects.
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <2>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault

# Serializer for classes with named companion objects are retrieved using `getDeclaredClasses`.
# If you have any, uncomment and replace classes with those containing named companion objects.
#-keepattributes InnerClasses # Needed for `getDeclaredClasses`.
#-if @kotlinx.serialization.Serializable class
#com.example.myapplication.HasNamedCompanion, # <-- List serializable classes with named companions.
#com.example.myapplication.HasNamedCompanion2
#{
#    static **$* *;
#}
#-keepnames class <1>$$serializer { # -keepnames suffices; class is kept when serializer() is kept.
#    static <1>$$serializer INSTANCE;
#}
##---------------End: kotlinx.serialization----------

##---------------  Begin: video player  ----------
-keep class com.google.gson.stream.** { *; }

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**  { *; }

#for iqiyi sdk
-keep class com.gala.**{*;}
-dontwarn com.gala.**
-keep class com.mcto.**{*;}
-keep class tv.gitv.ptqy.**{*;}
-keep class okhttp3.**{*;}
-keep class okio.**{*;}
-keep class org.xbill.**{*;}
-dontwarn tv.miplayer.**
-dontwarn org.xbill.**
-dontnote com.alibaba.**

-keep class cn.com.mma.**{*;}
-keep class cn.com.mmachina.**{*;}
-keep class com.netdoc.doc.**{*;}
-keep class com.qidun.**{*;}
-keep class com.qiyi.**{*;}
-keep class net.tsz.**{*;}
##---------------   End: video player  -----------


##---------------  Begin: DLNA cling  ----------
-keep class org.fourthline.cling.**{*;}
-keep class org.eclipse.jetty.**{*;}
-keep class org.seamless.**{*;}
# keep classes from `org.eclipse.jetty.orbit:javax.servlet`
-keep class javax.servlet.**{*;}

-dontwarn org.eclipse.jetty.**
-dontwarn org.fourthline.cling.**
-dontwarn org.seamless.**
-dontnote org.fourthline.cling.**
-dontnote org.seamless.**
##---------------  End: DLNA cling  ----------


##---------------  Begin: mibrain cling  ----------
-keep class com.fasterxml.jackson.databind.ext.**{*;}
-dontwarn com.fasterxml.jackson.databind.ext.**
## ---------------  End: mibrain cling  ----------


##---------------  Begin: protobuf  ----------
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
## ---------------  End: protobuf  ----------


-dontwarn org.apache.log4j.**
-keep class org.apache.log4j.** { *;}
-dontwarn com.loopj.android.http.**
-keep class com.loopj.android.http.** { *;}
-keep class org.mediasdk.voiceengine.** {*;}
-keep class org.mediasdk.videoengine.** {*;}
-keep class org.dom4j.** {*;}
-dontwarn org.dom4j.**
-keep class com.sun.jdi.** {*;}
-dontwarn com.sun.jdi.**
-keep class java.applet.** {*;}
-dontwarn java.applet.**

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep class com.zui.** {*;}
-keep class com.heytap.** {*;}
-keep class a.** {*;}
-keep class com.vivo.** {*;}

-keep class com.android.internal.** { *; }
-keepnames class * extends androidx.appcompat.app.AppCompatActivity
-keepnames class * extends android.app.Fragment
-keepnames class * extends androidx.fragment.app.Fragment


-dontwarn com.caverock.androidsvg.SVG
-dontwarn com.caverock.androidsvg.SVGParseException
-dontwarn pl.droidsonroids.gif.GifDrawable


#miot sdk
-keep class io.netty.** {*;}
-keep class org.slf4j.** {*;}
-keep class com.ouyang.** {*;}