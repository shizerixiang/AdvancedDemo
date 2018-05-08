package com.beviswang.nativelibrary

import android.util.Log
import org.jetbrains.anko.doAsync

object NativeMethod {
    init {
        System.loadLibrary("native-lib")
    }

    fun searchFileNative(dirPath: String){
        Log.e("searchFile","Path=$dirPath")
        doAsync {
            searchFile(dirPath)
        }
    }

    private external fun stringFromJNI(): String

    private external fun searchFile(dirPath: String)
}