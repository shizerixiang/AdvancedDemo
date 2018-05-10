package com.beviswang.advanceddemo

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast
import com.beviswang.capturelib.util.PermissionHelper
import com.beviswang.nativelibrary.IMiniWindowController
import com.beviswang.nativelibrary.NativeMethod
import com.beviswang.nativelibrary.service.MiniWindowService
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.newTask
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startService

class MainActivity : AppCompatActivity(), PermissionHelper.OnRequestPermissionsResultCallbacks {
    companion object {
        private const val OVERLAY_PERMISSION_REQ_CODE = 0x03
    }

    private var mIMiniWindowController: IMiniWindowController? = null
    private var mBound: Boolean = false
    private var isGrantedPermissions: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!PermissionHelper.requestPermissions(this, 0x02,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) requestDrawOverLays()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?, isAllDenied: Boolean) {
        finish()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?, isAllGranted: Boolean) {
        requestDrawOverLays()
    }

    private fun onGranted() {
        startService<MiniWindowService>()

        bindService(Intent().newTask().setClass(this, MiniWindowService::class.java),
                mServiceConnection, Context.BIND_AUTO_CREATE)

        mShowBtn.onClick {
            mIMiniWindowController?.showWindow()
        }
        mCloseBtn.onClick {
            mIMiniWindowController?.closeWindow()
        }
    }

    private fun scanDir() {
        // 700ms 左右，扫描 487016 个文件
        doAsync {
            NativeMethod.scanAllDir(Environment.getRootDirectory().absolutePath) // 49ms+
        }
    }

    /**
     * 申请悬浮窗权限
     */
    private fun requestDrawOverLays() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        isGrantedPermissions = true
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "请开启悬浮窗功能", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.packageName))
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
        } else onGranted()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBound)
            unbindService(mServiceConnection)
    }

    /**
     * The MiniWindowService connection.
     */
    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBound = true
            mIMiniWindowController = IMiniWindowController.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }
    }
}