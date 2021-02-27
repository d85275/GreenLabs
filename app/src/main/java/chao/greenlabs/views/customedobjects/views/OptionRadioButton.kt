package chao.greenlabs.views.customedobjects.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import chao.greenlabs.R
import chao.greenlabs.databinding.ViewOptionButtonBinding
import chao.greenlabs.datamodels.Option
import chao.greenlabs.viewmodels.ItemOptionsViewModel

open class OptionRadioButton : LinearLayout {

    private lateinit var binding: ViewOptionButtonBinding
    private lateinit var viewModel: ItemOptionsViewModel
    private lateinit var onCheckedAction: ((position: Int) -> Unit)
    private var optionPosition: Int = -1


    fun init(
        viewModel: ItemOptionsViewModel,
        optionPosition: Int,
        onCheckedAction: ((position: Int) -> Unit)
    ) {
        this.viewModel = viewModel
        this.optionPosition = optionPosition
        this.onCheckedAction = onCheckedAction
    }

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
        binding = ViewOptionButtonBinding.inflate(inflater, this, true)

        setListeners()
    }

    fun setOption(option: Option) {
        binding.option = option.title
        binding.price = context.getString(R.string.add_price, option.addPrice)
    }

    fun setChecked(isChecked: Boolean) {
        binding.radioButton.isChecked = isChecked
    }

    private fun setListeners() {
        binding.root.setOnClickListener {
            val isChecked = !binding.radioButton.isChecked
            if (isChecked) {
                onCheckedAction.invoke(optionPosition)
            } else {
                onCheckedAction.invoke(-1)
            }
        }
    }

}