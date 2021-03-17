package chao.market_helper.views.customedobjects.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import chao.market_helper.R
import chao.market_helper.databinding.ViewSetMarketInfoBinding
import chao.market_helper.utils.BottomSheetController
import chao.market_helper.utils.DialogUtils
import chao.market_helper.utils.InputChecker
import chao.market_helper.viewmodels.AddMarketViewModel
import chao.market_helper.views.customedobjects.IntNumWatcher

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
        val startTime = binding.tvStart.text.toString()
        val endTime = binding.tvEnd.text.toString()
        val fee = binding.etFee.text.toString()
        val date = binding.tvDate.text.toString()
        if (InputChecker.validInput(date, startTime, endTime, fee) && InputChecker.validNumber(fee)) {
            viewModel.addMarket(date, startTime, endTime, fee)
            dismiss()
        } else {
            val msg = context.getString(R.string.wrong_format)
            DialogUtils.showInfo(context, msg)
        }
    }

    private fun initView() {
        val inflater = LayoutInflater.from(context)
        binding = ViewSetMarketInfoBinding.inflate(inflater, this, true)
        binding.tvStart.text = context.getString(R.string.default_start_time)
        binding.tvEnd.text = context.getString(R.string.default_end_time)
        binding.etFee.addTextChangedListener(
            IntNumWatcher(
                binding.etFee
            )
        )
    }

    fun setDate(date: String) {
        binding.tvDate.text = date
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
}