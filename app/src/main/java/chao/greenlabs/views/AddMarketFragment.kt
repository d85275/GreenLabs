package chao.greenlabs.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.*
import chao.greenlabs.viewmodels.factories.AddMarketVMFactory
import chao.greenlabs.viewmodels.AddMarketViewModel
import kotlinx.android.synthetic.main.fragment_add_market.*
import kotlinx.android.synthetic.main.fragment_add_market.bt_confirm
import kotlinx.android.synthetic.main.fragment_add_market.et_name
import kotlinx.android.synthetic.main.fragment_add_market.et_fee
import kotlinx.android.synthetic.main.fragment_add_market.ll_back
import kotlinx.android.synthetic.main.fragment_add_market.tv_date

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
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(AddMarketViewModel::class.java)
    }

    private fun registerObservers() {
        viewModel.getMessage().observe(viewLifecycleOwner, Observer { msg ->
            ToastUtils.show(requireContext(), msg)
            et_name.text.clear()
            et_location.text.clear()
            et_fee.text.clear()
            et_name.requestFocus()
        })
    }

    private fun setDate() {
        tv_date.text = DateTimeUtils.getCurrentDate()
    }

    private fun setListeners() {
        ll_date.setOnClickListener {
            showDatePicker()
        }

        bt_confirm.setOnClickListener {
            val name = et_name.text.toString()
            val fee = et_fee.text.toString()
            val date = tv_date.text.toString()
            val location = et_location.text.toString()
            if (InputChecker.validMarketItem(name, location, fee)) {
                viewModel.addMarket(name, fee, location, date)
            } else {
                DialogUtils.showWrongFormat(requireContext())
            }
        }

        ll_back.setOnClickListener {
            KeyboardUtils.hideKeyboard(requireContext(), view)
            findNavController().popBackStack()
        }

        ll_next.setOnClickListener {
            val name = et_name.text.toString()
            val location = et_location.text.toString()
            if (InputChecker.validInput(name, location)) {
                viewModel.marketName = name
                viewModel.marketLocation = location
                KeyboardUtils.hideKeyboard(requireContext(), view)
                findNavController().navigate(R.id.action_addMarketFragment_to_addMarketSetDateFragment)
            } else {
                DialogUtils.showWrongFormat(requireContext())
            }

        }

        et_fee.addTextChangedListener(textWatcher)
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(requireContext())
        datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
            tv_date.text = DateTimeUtils.getDateString(year, month, dayOfMonth)
        }
        datePicker.show()
    }

    private fun showKeyboard() {
        et_name.requestFocus()
        KeyboardUtils.showKeyboard(requireContext())
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            var text = s.toString()
            if (text.contains(".")) {
                text = text.replace(".", "")
                et_fee.setText(text)
                et_fee.setSelection(et_fee.text.length)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}