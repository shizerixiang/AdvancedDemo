package com.beviswang.nativelibrary

import android.util.Log
import org.jetbrains.anko.doAsync
import java.io.File

object NativeMethod {
    init {
        System.loadLibrary("native-lib")
    }

    /**
     * Search file by c/c++.
     */
    fun searchFileNative(dirPath: String) {
        Log.e("searchFile", "Path=$dirPath")
        doAsync {
            searchFile(dirPath)
        }
    }

    /**
     * Search file by Java.
     */
    fun searchFileJava(dirPath: String) {
        val dir = File(dirPath)
        if (!dir.isDirectory) throw Exception("The file is not directory!")
        val files = dir.listFiles() ?: return
        files.forEach {
            if (it.isDirectory) {
                searchFileJava(it.absolutePath)
            } else {
                Log.d("searchFile", "file: ${it.name}")
            }
        }
    }

    private external fun stringFromJNI(): String

    private external fun searchFile(dirPath: String)
}