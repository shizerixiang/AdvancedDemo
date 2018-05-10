package com.beviswang.nativelibrary.widget

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.beviswang.nativelibrary.R

/**
 * The window of service.
 * @author BevisWang
 */
class ServiceWindow(context: Context) : IServiceWindow {
    private var mWindowManager: WindowManager? = null
    private var application: Context = context.applicationContext
    private var windowView: View? = null
    private var isShowWindow: Boolean = false

    override fun show() {
        if (isShowWindow) return
        mWindowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (windowView == null)
            windowView = LayoutInflater.from(application).inflate(R.layout.window_service, null)
        val params = WindowManager.LayoutParams()
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        params.gravity = Gravity.END or Gravity.BOTTOM
        // 屏蔽点击事件
        params.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        mWindowManager?.addView(windowView, params)
        isShowWindow = true
    }

    override fun dismiss() {
        if (mWindowManager != null && windowView != null) {
            mWindowManager?.removeView(windowView)
        }
        isShowWindow = false
    }
}