package com.beviswang.advanceddemo

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.beviswang.capturelib.util.PermissionHelper
import com.beviswang.nativelibrary.NativeMethod
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity(), PermissionHelper.OnRequestPermissionsResultCallbacks {
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?, isAllDenied: Boolean) {
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?, isAllGranted: Boolean) {
        onGranted()
    }

    private fun onGranted() {
        // 700ms 左右，扫描 487016 个文件
//        NativeMethod.searchFileNative(Environment.getRootDirectory().absolutePath) // 49ms+
        doAsync {
            NativeMethod.searchFileJava(Environment.getRootDirectory().absolutePath) // 88ms+
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (PermissionHelper.requestPermissions(this, 0x02,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) onGranted() else finish()
    }
}