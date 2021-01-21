package chao.greenlabs.views

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.DateUtils
import chao.greenlabs.utils.KeyboardUtils
import chao.greenlabs.utils.ToastUtils
import chao.greenlabs.viewmodels.factories.AddMarketVMFactory
import chao.greenlabs.viewmodels.AddMarketViewModel
import kotlinx.android.synthetic.main.fragment_add_market.*

class AddMarketFragment : Fragment() {

    private lateinit var viewModel: AddMarketViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_market, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel()
        registerObservers()
        setDate()
        setListeners()
        showKeyboard()
    }

    private fun getViewModel() {
        val factory = AddMarketVMFactory(
            Repository(requireContext())
        )
        viewModel = ViewModelProvider(this, factory).get(AddMarketViewModel::class.java)
    }

    private fun registerObservers() {
        viewModel.getMessage().observe(viewLifecycleOwner, Observer { msg ->
            ToastUtils.show(requireContext(), msg)
            et_name.text.clear()
            et_price.text.clear()
            et_name.requestFocus()
        })
    }

    private fun setDate() {
        tv_date.text = DateUtils.getCurrentDate()
    }

    private fun setListeners() {
        ll_date.setOnClickListener {
            showDatePicker()
        }

        bt_confirm.setOnClickListener {
            val name = et_name.text.toString()
            val price = et_price.text.toString()
            val date = tv_date.text.toString()
            viewModel.addMarket(name, price, date)
        }

        ll_back.setOnClickListener {
            KeyboardUtils.hideKeyboard(requireContext(), view)
            findNavController().popBackStack()
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(requireContext())
        datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
            tv_date.text = DateUtils.onDateChanged(year, month, dayOfMonth)
        }
        datePicker.show()
    }

    private fun showKeyboard() {
        et_name.requestFocus()
        KeyboardUtils.showKeyboard(requireContext())
    }
}