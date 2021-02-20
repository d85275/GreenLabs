package chao.greenlabs.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import chao.greenlabs.R
import chao.greenlabs.views.MainActivity

open class BaseFragment : Fragment() {

    private var contentViewId: Int = -1

    fun setContentViewId(id: Int) {
        contentViewId = id
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(contentViewId, container, false)
    }

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