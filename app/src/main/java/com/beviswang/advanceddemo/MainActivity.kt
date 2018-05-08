package com.beviswang.advanceddemo

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.beviswang.capturelib.util.PermissionHelper
import com.beviswang.nativelibrary.NativeMethod
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PermissionHelper.OnRequestPermissionsResultCallbacks {
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?, isAllDenied: Boolean) {
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?, isAllGranted: Boolean) {
        onGranted()
    }

    private fun onGranted() {
        NativeMethod.searchFileNative("/")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (PermissionHelper.requestPermissions(this, 0x02,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) onGranted() else finish()
    }
}
