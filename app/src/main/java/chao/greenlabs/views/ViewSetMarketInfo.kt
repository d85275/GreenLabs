package chao.greenlabs.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import chao.greenlabs.databinding.ViewSetMarketInfoBinding
import chao.greenlabs.utils.BottomSheetController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.view_set_market_info.view.*

open class ViewSetMarketInfo : LinearLayout {

    private lateinit var binding: ViewSetMarketInfoBinding
    private lateinit var bottomSheetController: BottomSheetController<ViewSetMarketInfo>

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
        binding.btConfirm.setOnClickListener {
            // todo: to check if we should add the data
            bottomSheetController.hide()
        }
    }

    private fun initView() {
        val inflater = LayoutInflater.from(context)
        binding = ViewSetMarketInfoBinding.inflate(inflater, this, true)
    }

    fun setDate(date: String) {
        tv_date.text = date
    }

    fun setBottomSheetController(controller: BottomSheetController<ViewSetMarketInfo>) {
        bottomSheetController = controller
    }

}