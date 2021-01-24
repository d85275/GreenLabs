package chao.greenlabs.views

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
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
        binding.tvStart.setOnClickListener { }
        binding.tvEnd.setOnClickListener { }
        binding.tvAdd.setOnClickListener {
            addMarket()
        }
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
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

    fun dismiss(){
        bottomSheetController.hide()
    }
}