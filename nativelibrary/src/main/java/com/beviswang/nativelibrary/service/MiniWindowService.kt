package com.beviswang.nativelibrary.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.beviswang.nativelibrary.IMiniWindowController
import com.beviswang.nativelibrary.widget.IServiceWindow
import com.beviswang.nativelibrary.widget.ServiceWindow
import org.jetbrains.anko.runOnUiThread

class MiniWindowService : Service() {
    private var serviceWindow: IServiceWindow? = null

    override fun onCreate() {
        super.onCreate()
        showServiceWindow()
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    private fun showServiceWindow() {
        if (serviceWindow == null)
            serviceWindow = ServiceWindow(this)
        serviceWindow?.show()
    }

    private fun closeServiceWindow() {
        serviceWindow?.dismiss()
        serviceWindow = null
    }

    override fun onDestroy() {
        super.onDestroy()
        closeServiceWindow()
    }

    /**
     * AIDL for main process control.
     */
    private val mBinder = object : IMiniWindowController.Stub() {
        override fun showWindow() {
            runOnUiThread {
                showServiceWindow()
            }
        }

        override fun closeWindow() {
            runOnUiThread {
                closeServiceWindow()
            }
        }
    }
}
