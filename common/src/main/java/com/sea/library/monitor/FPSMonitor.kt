package com.sea.library.monitor

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.Choreographer
import com.sea.library.common.logger.Logger

object FPSMonitor {

    private const val FPS_INTERVAL_TIME = 1000L

    private var openFps = false
    private val fpsRunnable = FpsRunnable()
    private val mainLooper = Looper.getMainLooper()
    private val mainHandler by lazy { Handler(mainLooper) }

    private val listeners = arrayListOf<(Int) -> Unit>()

    fun start(listener: (Int) -> Unit) {
        if ( ! openFps ) {
            openFps = true
            listeners.add(listener)
            mainHandler.postDelayed(fpsRunnable, FPS_INTERVAL_TIME)
            Choreographer.getInstance().postFrameCallback(fpsRunnable)
        }
    }

    fun stop() {
        mainHandler.removeCallbacks(fpsRunnable)
        Choreographer.getInstance().removeFrameCallback(fpsRunnable)
        openFps = false
    }

    private class FpsRunnable : Choreographer.FrameCallback, Runnable {

        private var time = 0L
        private var count = 0

        override fun doFrame(frameTimeNanos: Long) {
            count++
            Choreographer.getInstance().postFrameCallback(this)
        }

        override fun run() {
            val currentTime = SystemClock.elapsedRealtime()
            if (time == 0L) {
                // 第一次开始监控 跳过
            } else {
                val fps = (1000f * count / (currentTime - time) + 0.5f).toInt()
                val fpsString = String.format("APP FPS is: %-3sHz", fps)
                if (fps <= 50) {
                    Logger.e("FPS", fpsString)
                } else {
                    Logger.w("FPS", fpsString)
                }
            }
            count = 0
            time = currentTime
            listeners.forEach { it.invoke(count) }
            mainHandler.postDelayed(this, FPS_INTERVAL_TIME)
        }

    }

}