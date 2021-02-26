package chao.greenlabs.views.customedobjects.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import chao.greenlabs.databinding.ItemOptionButtonBinding

open class ItemRadioButton : LinearLayout {

    private lateinit var binding: ItemOptionButtonBinding

    constructor(context: Context?) : super(context) {
        getViews()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        getViews()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        getViews()
    }

    private fun getViews() {
        val inflater = LayoutInflater.from(context)
        binding = ItemOptionButtonBinding.inflate(inflater, this, true)
        setListeners()
    }

    fun setData(option: String, price: String) {
        binding.option = option
        binding.price = price
    }

    private fun setListeners() {
        binding.tvPrice.setOnClickListener {
            binding.radioButton.isChecked = !binding.radioButton.isChecked
        }
    }
}