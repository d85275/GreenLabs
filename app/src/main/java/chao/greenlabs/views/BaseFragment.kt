package chao.greenlabs.views

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    fun showLoading() {
        if (activity != null && activity is MainActivity) {
            (activity as MainActivity).showLoading()
        }
    }

    fun dismissLoading() {
        if (activity != null && activity is MainActivity) {
            (activity as MainActivity).dismissLoading()
        }
    }
}