package com.beviswang.nativelibrary.broadcast

import android.content.*
import com.beviswang.nativelibrary.service.MiniWindowService
import org.jetbrains.anko.newTask

/**
 * Boot start completed receiver.
 * @author BevisWang
 */
class BootCompletedReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_BOOT_COMPLETED: String = "android.intent.action.BOOT_COMPLETED"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        if (intent.action != ACTION_BOOT_COMPLETED || context == null) return
        doSth(context)
    }

    /**
     * The method command when start boot.
     * @param context The application context.
     */
    private fun doSth(context: Context) {
        context.startService(Intent().newTask().setClass(context,MiniWindowService::class.java))
    }
}