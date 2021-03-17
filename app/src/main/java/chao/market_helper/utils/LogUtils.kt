package chao.market_helper.utils

import android.util.Log

private const val TAG = "LogUtils"
object LogUtils {
    fun debug(msg: String) {
        Log.e(TAG, "DEBUG: $msg")
    }
}