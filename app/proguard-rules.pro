# Room KMP Keep Rules
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# Ktor & kotlinx.serialization Keep Rules
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}
-keep class kotlinx.serialization.** { *; }

# Domain & Data Models Keep Rules
-keep class com.shelf.data.local.entity.** { *; }
-keep class com.shelf.data.remote.dto.** { *; }
-keep class com.shelf.domain.model.** { *; }
