# ProGuard rules for OMDB TV

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
    <init>();
    public void applyOptions(android.content.Context, com.bumptech.glide.GlideBuilder);
    public void registerComponents(android.content.Context, com.bumptech.glide.Glide, com.bumptech.glide.Registry);
}
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keep class com.example.omdbtv.api.** { *; }

# Keep model classes
-keep class com.example.omdbtv.data.** { *; }
