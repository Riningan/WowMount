-include proguard-rules.pro
-keepattributes SourceFile,LineNumberTable

-ignorewarnings

-keepattributes *Annotation*

-dontnote junit.framework.**
-dontnote junit.runner.**

-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
-dontwarn org.hamcrest.**
-dontwarn com.squareup.javawriter.JavaWriter
-dontwarn java.beans.**
-dontwarn javax.lang.model.element.Modifier
-dontwarn org.apache.tools.ant.**
-dontwarn org.assertj.core.internal.cglib.asm.util.TraceClassVisitor
-dontwarn io.mockk.**
-dontwarn org.jmock.core.**
-dontwarn org.mockito.**
-dontwarn org.awaitility.core.**
-dontwarn org.bouncycastle.jce.**
-dontwarn org.bouncycastle.x509.**
-dontwarn org.w3c.dom.bootstrap.**
-dontwarn sun.misc.Unsafe
-dontwarn sun.reflect.**