package chao.greenlabs.views.fragments

import androidx.fragment.app.Fragment
import chao.greenlabs.views.MainActivity

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