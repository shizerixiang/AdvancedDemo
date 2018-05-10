#include <jni.h>
#include <string>
#include "fileSearch.cpp"

extern "C"
JNIEXPORT jstring JNICALL Java_com_beviswang_nativelibrary_NativeMethod_stringFromJNI(
        JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL Java_com_beviswang_nativelibrary_NativeMethod_scanAllDir(
        JNIEnv *env, jobject /* this */, jstring dir_name) {
    jboolean iscopy;
    const char *dir_name_chars = (*env).GetStringUTFChars(dir_name, &iscopy);
//    showAllFiles(dir_name_chars);
    scan_dir(dir_name_chars,0);
    (*env).ReleaseStringUTFChars(dir_name, dir_name_chars);
}
