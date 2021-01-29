package chao.greenlabs.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import chao.greenlabs.R
import chao.greenlabs.databinding.ViewSetMarketInfoBinding
import chao.greenlabs.utils.BottomSheetController
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.utils.InputChecker
import chao.greenlabs.viewmodels.AddMarketViewModel

open class ViewSetMarketInfo : LinearLayout {

    private lateinit var binding: ViewSetMarketInfoBinding
    private lateinit var bottomSheetController: BottomSheetController<ViewSetMarketInfo>
    private lateinit var viewModel: AddMarketViewModel

    fun init(
        date: String,
        controller: BottomSheetController<ViewSetMarketInfo>,
        viewModel: AddMarketViewModel
    ) {
        binding.tvDate.text = date
        binding.tvLocation.text = viewModel.marketLocation
        bottomSheetController = controller
        this.viewModel = viewModel
    }

    constructor(context: Context?) : super(context) {
        onViewCreated()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        onViewCreated()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        onViewCreated()
    }

    private fun onViewCreated() {
        initView()
        setListeners()
    }

    private fun setListeners() {
        binding.tvStart.setOnClickListener {
            showTimePicker(binding.tvStart)
        }
        binding.tvEnd.setOnClickListener {
            showTimePicker(binding.tvEnd)
        }
        binding.tvConfirm.setOnClickListener {
            addMarket()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun showTimePicker(textView: TextView) {
        val hour = textView.text.split(":")[0].trim()
        val min = textView.text.split(":")[1].trim()

        DialogUtils.showTimePicker(context, hour, min) { h, m ->
            textView.text = context.getString(R.string.market_time, h, m)
        }
    }

    private fun addMarket() {
        // todo: to check if we should add the data
        val startTime = binding.tvStart.text.toString()
        val endTime = binding.tvEnd.text.toString()
        val fee = binding.etFee.text.toString()
        val date = binding.tvDate.text.toString()
        if (InputChecker.validInput(date, startTime, endTime, fee)) {
            viewModel.addMarket(date, startTime, endTime, fee)
        }
        dismiss()
    }

    private fun initView() {
        val inflater = LayoutInflater.from(context)
        binding = ViewSetMarketInfoBinding.inflate(inflater, this, true)
        binding.tvStart.text = context.getString(R.string.default_start_time)
        binding.tvEnd.text = context.getString(R.string.default_end_time)
        binding.etFee.addTextChangedListener(textWatcher)
    }

    fun setDate(date: String) {
        binding.tvDate.text = date
    }

    fun requireFocus() {
        binding.etFee.requestFocus()
    }

    fun dismiss() {
        bottomSheetController.hide()
    }

    fun isViewShown(): Boolean {
        return bottomSheetController.isShown()
    }

    fun setConfirmButtonText(text: String) {
        binding.tvConfirm.text = text
    }

    fun setFee(fee: String) {
        binding.etFee.setText(fee)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            var text = s.toString()
            if (text.contains(".")) {
                text = text.replace(".", "")
                binding.etFee.setText(text)
                binding.etFee.setSelection(binding.etFee.text.length)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}