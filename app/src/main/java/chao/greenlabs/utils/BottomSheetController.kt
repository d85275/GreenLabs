package chao.greenlabs.utils

import android.util.Log
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetController<T : View?>(view: T) {
    private var bottomSheetBehavior: BottomSheetBehavior<T> = BottomSheetBehavior.from(view)

    init {
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN ||
                    bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED
                ) {
                    KeyboardUtils.hideKeyboard(bottomSheet.context, bottomSheet)
                }
            }
        })
    }

    fun isShown(): Boolean {
        return bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED
    }

    fun hide() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    fun show() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN ||
            bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED
        ) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

}