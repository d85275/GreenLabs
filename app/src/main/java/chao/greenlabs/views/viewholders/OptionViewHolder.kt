package chao.greenlabs.views.viewholders

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.databinding.ItemOptionsBinding
import chao.greenlabs.datamodels.OptionCategory
import chao.greenlabs.datamodels.Option
import chao.greenlabs.viewmodels.ItemOptionsViewModel
import chao.greenlabs.views.customedobjects.views.OptionRadioButton

class OptionViewHolder(private val binding: ItemOptionsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var optionCategory: OptionCategory
    private lateinit var viewModel: ItemOptionsViewModel
    private var categoryPosition = -1

    fun bindView(
        optionCategory: OptionCategory,
        position: Int,
        viewModel: ItemOptionsViewModel
    ) {
        this.optionCategory = optionCategory
        this.viewModel = viewModel
        this.categoryPosition = position
        setViews()
    }

    private fun setViews() {
        binding.tvTitle.text = optionCategory.title
        binding.rgDetails.removeAllViews()
        for (i in optionCategory.optionList.indices) {
            addRadioButton(optionCategory.optionList[i], i)
        }
    }

    private fun addRadioButton(option: Option, optionPosition: Int) {
        if (option.title.isEmpty()) return
        val itemRadioButton = OptionRadioButton(binding.root.context)

        itemRadioButton.init(viewModel, optionPosition) { position ->
            viewModel.setSelection(categoryPosition, position)
            updateCheck(position)
        }

        itemRadioButton.setOption(option)

        itemRadioButton.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        binding.rgDetails.addView(itemRadioButton)
    }

    private fun updateCheck(optionPosition: Int) {
        val count = binding.rgDetails.childCount
        for (i in 0 until count) {
            val radioButton = binding.rgDetails.getChildAt(i) as OptionRadioButton
            radioButton.setChecked(optionPosition == i)
        }
    }
}