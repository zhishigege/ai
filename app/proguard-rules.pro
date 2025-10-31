# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontusemixedcaseclassnames
-verbose

# Preserve line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep all public classes and methods
-keep public class * {
    public protected *;
}

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep constructors for serialization
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# Keep enum values and valueOf() methods
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Room database classes
-keep class androidx.room.** { *; }
-keepclasseswithmembers class * {
    @androidx.room.* <methods>;
}

# Keep Retrofit interfaces
-keep interface com.aiefficiency.api.** { *; }
-keep class com.aiefficiency.model.** { *; }

# Keep Gson annotations
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }

# Keep Kotlin metadata
-keepattributes *Annotation*
-keep class kotlin.Metadata { *; }
