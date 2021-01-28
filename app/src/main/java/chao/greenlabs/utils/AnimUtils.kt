package chao.greenlabs.utils

import android.view.View

object AnimUtils {
    private const val SHOW_DURATION_MS = 600L
    private const val OFFSET = -75f

    fun showMarketDetail(parent: View, view: View) {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
        )
        view.translationX = (-2 * view.measuredWidth).toFloat()
        view.visibility = View.VISIBLE
        view.animate()
            .translationX(OFFSET)
            .setDuration(SHOW_DURATION_MS)
            .start()
    }
}