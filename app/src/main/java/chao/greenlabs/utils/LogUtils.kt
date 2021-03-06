package chao.greenlabs.utils

import android.util.Log

private const val TAG = "LogUtils"
object LogUtils {
    fun debug(msg: String) {
        Log.e(TAG, "DEBUG: $msg")
    }
}