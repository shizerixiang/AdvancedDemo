package com.beviswang.nativelibrary

object NativeMethod {
    init {
        System.loadLibrary("native-lib")
    }

    external fun stringFromJNI(): String

    /**
     * Scanning all file in the path.
     * I/O Operation.
     * @param dirPath Direction path.
     */
    external fun scanAllDir(dirPath: String)
}