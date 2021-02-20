package chao.greenlabs.views.customedobjects

import android.view.View
import androidx.viewpager2.widget.ViewPager2


class SoldItemTransformer(private val offscreenPageLimit: Int) : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {

        page.apply {
            val myOffset = position * -(page.width / 3 * 2)
            when {
                position <= 0f -> {
                    page.translationX = -myOffset
                }
                position <= offscreenPageLimit - 1 -> {
                    page.translationX = myOffset
                    page.alpha = 1f
                }
                else -> {
                    page.alpha = 0f
                    page.translationX = myOffset
                }
            }
        }
    }
}