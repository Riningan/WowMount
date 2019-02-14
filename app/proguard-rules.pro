-dontobfuscate
-optimizations !class/unboxing/enum

# for androidTest
# java.lang.NoSuchMethodError: No virtual method getKodein()Lorg/kodein/di/conf/ConfigurableKodein; in class Lcom/riningan/wowmount/app/WowMountApp; or its super classes (declaration of 'com.riningan.wowmount.app.WowMountApp' appears in /data/app/com.riningan.wowmount-1/base.apk)
# java.lang.NoSuchMethodError: No static method getDataModule()Lorg/kodein/di/Kodein$Module; in class Lcom/riningan/wowmount/app/di/DataModuleKt; or its super classes (declaration of 'com.riningan.wowmount.app.di.DataModuleKt' appears in /data/app/com.riningan.wowmount-1/base.apk)
# java.lang.NoSuchMethodError: No virtual method setFragment(Ljava/lang/Class;Ljava/lang/Object;)V in class Lcom/riningan/wowmount/test/TestActivity; or its super classes (declaration of 'com.riningan.wowmount.test.TestActivity' appears in /data/app/com.riningan.wowmount-2/base.apk)
# java.lang.NoSuchMethodError: No virtual method setEnabled(Z)Lcom/riningan/util/LoggerConfig; in class Lcom/riningan/util/LoggerConfig; or its super classes (declaration of 'com.riningan.util.LoggerConfig' appears in /data/app/com.riningan.wowmount-2/base.apk)
-keep class com.riningan.wowmount.** { *; }