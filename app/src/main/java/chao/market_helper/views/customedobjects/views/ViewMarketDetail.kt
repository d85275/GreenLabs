package chao.market_helper.views.customedobjects.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import chao.market_helper.databinding.ViewMarketDetailBinding
import chao.market_helper.datamodels.MarketData
import chao.market_helper.utils.AnimUtils
import chao.market_helper.utils.DialogUtils
import chao.market_helper.viewmodels.ManageMarketViewModel

private const val MARKET_DETAIL_VIEW_OFFSET = 75f

open class ViewMarketDetail : LinearLayout {

    private lateinit var binding: ViewMarketDetailBinding
    private lateinit var parent: View
    private lateinit var viewModel: ManageMarketViewModel
    private var isMarketDetailShown = false

    fun init(parent: View, viewModel: ManageMarketViewModel) {
        this.parent = parent
        this.viewModel = viewModel
        AnimUtils.setViewToRight(parent, this)
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

    private fun initView() {
        val inflater = LayoutInflater.from(context)
        binding = ViewMarketDetailBinding.inflate(inflater, this, true)
    }

    private fun setListeners() {
        binding.tvMarketLocation.setOnClickListener {
            val location = binding.tvMarketLocation.text.toString()
            DialogUtils.showEditText(context, {
                viewModel.updateMarketLocation(it)
            }, location)
        }

        binding.tvMarketFee.setOnClickListener {
            val price = binding.tvMarketFee.text.toString()
            DialogUtils.showEditNumber(context, {
                viewModel.updateMarketFee(it)
            }, price)
        }

        binding.tvStartTime.setOnClickListener {
            val hour = binding.tvStartTime.text.toString().split(":")[0].trim()
            val min = binding.tvStartTime.text.toString().split(":")[1].trim()
            DialogUtils.showTimePicker(context, hour, min) { h, m ->
                viewModel.updateMarketStartTime(h, m)
            }
        }

        binding.tvEndTime.setOnClickListener {
            val hour = binding.tvEndTime.text.toString().split(":")[0].trim()
            val min = binding.tvEndTime.text.toString().split(":")[1].trim()
            DialogUtils.showTimePicker(context, hour, min) { h, m ->
                viewModel.updateMarketEndTime(h, m)
            }
        }
    }

    fun setMarketData(data: MarketData) {
        binding.tvStartTime.text = data.startTime
        binding.tvEndTime.text = data.endTime
        binding.tvMarketFee.text = data.fee
        binding.tvMarketLocation.text = data.location
    }

    fun showMarketDetail() {
        isMarketDetailShown = if (isMarketDetailShown) {
            AnimUtils.hideToRight(parent, this)
            false
        } else {
            AnimUtils.showFromRight(parent, this, MARKET_DETAIL_VIEW_OFFSET)
            true
        }
    }
}