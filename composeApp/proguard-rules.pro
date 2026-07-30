# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-dontnote kotlinx.serialization.Serializable

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.escolaapp.**$$serializer { *; }
-keepclassmembers class com.escolaapp.** {
    *** Companion;
}
-keepclasseswithmembers class com.escolaapp.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Koin
-keep class org.koin.** { *; }

# Voyager
-keep class cafe.adriel.voyager.** { *; }

# Ktor
-keep class io.ktor.** { *; }

# Lyricist
-keep class cafe.adriel.lyricist.** { *; }

# Kotlinx DateTime
-keep class kotlinx.datetime.** { *; }

# Compose
-keep class androidx.compose.** { *; }
