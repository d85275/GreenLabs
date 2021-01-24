package chao.greenlabs.views

import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import chao.greenlabs.databinding.ViewSetMarketInfoBinding
import chao.greenlabs.utils.BottomSheetController
import chao.greenlabs.utils.DateTimeUtils
import chao.greenlabs.viewmodels.AddMarketSetDateViewModel

open class ViewSetMarketInfo : LinearLayout {

    private lateinit var binding: ViewSetMarketInfoBinding
    private lateinit var bottomSheetController: BottomSheetController<ViewSetMarketInfo>
    private lateinit var viewModel: AddMarketSetDateViewModel

    fun init(
        date: String,
        controller: BottomSheetController<ViewSetMarketInfo>,
        viewModel: AddMarketSetDateViewModel
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
        binding.tvAdd.setOnClickListener {
            addMarket()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun showTimePicker(textView: TextView) {
        val hour = textView.text.split(":")[0].trim()
        val min = textView.text.split(":")[1].trim()
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val h = if (hourOfDay > 10) hourOfDay.toString() else "0$hourOfDay"
                val m = if (minute > 10) minute.toString() else "0$minute"
                textView.text = "$h : $m"
            },
            hour.toInt(),
            min.toInt(),
            true
        )

        timePickerDialog.show()
    }

    private fun addMarket() {
        // todo: to check if we should add the data
        val startTime = binding.tvStart.text.toString()
        val endTime = binding.tvEnd.text.toString()
        val fee = binding.etFee.text.toString()
        val date = binding.tvDate.text.toString()
        viewModel.addMarket(date, startTime, endTime, fee)
        dismiss()
    }

    private fun initView() {
        val inflater = LayoutInflater.from(context)
        binding = ViewSetMarketInfoBinding.inflate(inflater, this, true)
        binding.tvStart.text = DateTimeUtils.getCurrentTime()
        binding.tvEnd.text = DateTimeUtils.getCurrentTime()
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
}