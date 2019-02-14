# Proguard Configuration for Realm (http://realm.io)
# For detailed discussion see: https://groups.google.com/forum/#!topic/realm-java/umqKCc50JGU
# Additionally you need to keep your Realm Model classes as well
# For example:
# -keep class com.yourcompany.realm.** { *; }

-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class *
-dontwarn javax.**
-dontwarn io.realm.**

# for androidTest
-keep class io.realm.RealmConfiguration$Builder { *; }
-keep class io.realm.ImportFlag
-keep class io.realm.RealmModel
-keep class io.realm.Realm {
    public io.realm.RealmModel copyToRealm(io.realm.RealmModel, io.realm.ImportFlag[]);
}