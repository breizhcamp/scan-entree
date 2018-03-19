# Ensure stacktraces can be completely deciphered
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable,Signature
-keepclassmembers,allowshrinking,allowobfuscation class com.android.volley.NetworkDispatcher {
    void processRequest();
}
-keepclassmembers,allowshrinking,allowobfuscation class com.android.volley.CacheDispatcher {
    void processRequest();
}