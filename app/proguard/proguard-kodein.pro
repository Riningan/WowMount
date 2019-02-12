# Kodein
-keepattributes Signature

-keep class org.kodein.di.** { *; }
-keep class org.kodein.di.conf.** { *; }
-keep class org.kodein.di.conf.ConfigurableKodein { *; }
-keep interface org.kodein.di.** { *; }

-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}