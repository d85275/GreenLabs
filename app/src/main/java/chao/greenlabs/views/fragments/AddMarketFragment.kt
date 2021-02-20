package chao.greenlabs.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.*
import chao.greenlabs.viewmodels.AddMarketViewModel
import chao.greenlabs.viewmodels.factories.AddMarketSetDateVMFactory
import kotlinx.android.synthetic.main.fragment_add_market.*
import kotlinx.android.synthetic.main.fragment_add_market.bt_confirm
import kotlinx.android.synthetic.main.fragment_add_market.et_name
import kotlinx.android.synthetic.main.fragment_add_market.ll_back

class AddMarketFragment : BaseFragment() {

    private lateinit var addMarketViewModel: AddMarketViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_add_market)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel()
        setListeners()
        showKeyboard()
    }

    private fun getViewModel() {
        val factory = AddMarketSetDateVMFactory(
            Repository(requireContext())
        )

        addMarketViewModel =
            ViewModelProvider(requireActivity(), factory).get(AddMarketViewModel::class.java)
    }

    private fun setListeners() {
        bt_confirm.setOnClickListener {
            goNext()
        }

        ll_back.setOnClickListener {
            KeyboardUtils.hideKeyboard(requireContext(), view)
            findNavController().popBackStack()
        }

        ll_next.setOnClickListener {
            goNext()
        }
    }

    private fun goNext() {
        val name = et_name.text.toString()
        val location = et_location.text.toString()
        if (InputChecker.validInput(name, location)) {
            addMarketViewModel.marketName = name
            addMarketViewModel.marketLocation = location
            KeyboardUtils.hideKeyboard(requireContext(), view)
            findNavController().navigate(R.id.action_addMarketFragment_to_addMarketSetDateFragment)
        } else {
            DialogUtils.showWrongFormat(requireContext())
        }
    }

    private fun showKeyboard() {
        et_name.requestFocus()
        KeyboardUtils.showKeyboard(requireContext(), et_name)
    }
}