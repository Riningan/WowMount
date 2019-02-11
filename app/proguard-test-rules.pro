# Proguard rules run against the test module (split since Gradle plugin v 1.1.0)
-dontobfuscate
-ignorewarnings

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

# Specific classes that common test libs warn about
-dontwarn java.beans.**
-dontwarn javax.lang.model.element.Modifier
-dontwarn org.apache.tools.ant.**
-dontwarn org.assertj.core.internal.cglib.asm.util.TraceClassVisitor
-dontwarn org.easymock.**
-dontwarn io.mockk.**
-dontwarn org.jmock.core.**
-dontwarn org.w3c.dom.bootstrap.**
-dontwarn sun.misc.Unsafe
-dontwarn sun.reflect.**