package chao.market_helper.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {
    fun show(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}