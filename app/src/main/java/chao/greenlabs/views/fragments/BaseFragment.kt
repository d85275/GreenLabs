package chao.greenlabs.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import chao.greenlabs.views.MainActivity

open class BaseFragment : Fragment() {

    private var contentViewId: Int = -1
    private var onBackPressedAction: (() -> Unit)? = null

    fun setContentViewId(id: Int) {
        contentViewId = id
    }

    fun setOnBackPressedAction(action: (() -> Unit)) {
        onBackPressedAction = action
    }

    fun removeBackCallback() {
        backPressedCallback.remove()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            backPressedCallback
        )
        return inflater.inflate(contentViewId, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressedCallback.remove()
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

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (onBackPressedAction == null) {
                findNavController().popBackStack()
            } else {
                onBackPressedAction?.invoke()
            }
        }
    }
}