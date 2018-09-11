package com.ansgar.memorymanager

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*


internal class MemoryManagerService : Service() {

    private val tag = MemoryManagerService::class.java.canonicalName
    private var timer: Timer? = null

    override fun onCreate() {
        timer = Timer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val stringBuilder = StringBuilder()
                stringBuilder.append(MemoryManagerUtil.getAppMemoryUsage())
                        .append("\n")
                        .append(MemoryManagerUtil.getCpuAppUsage())
                        .append("\n")
                val responseIntent = Intent()
                responseIntent.action = Constants.EXTRA_ACTION
                responseIntent.putExtra(Constants.EXTRA_MEMORY_USAGE_DATA,
                        stringBuilder.toString())
                sendBroadcast(responseIntent)
            }
        }, 1000, MemoryManager.delay)

        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}