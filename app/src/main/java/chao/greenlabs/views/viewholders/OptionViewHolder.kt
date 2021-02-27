package chao.greenlabs.views.viewholders

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.databinding.ItemOptionsBinding
import chao.greenlabs.datamodels.ItemOptions
import chao.greenlabs.datamodels.Option
import chao.greenlabs.viewmodels.ItemOptionsViewModel
import chao.greenlabs.views.customedobjects.views.OptionRadioButton

class OptionViewHolder(private val binding: ItemOptionsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var itemOptions: ItemOptions
    private lateinit var viewModel: ItemOptionsViewModel
    private var itemPosition = -1

    fun bindView(
        itemOptions: ItemOptions,
        position: Int,
        viewModel: ItemOptionsViewModel
    ) {
        this.itemOptions = itemOptions
        this.viewModel = viewModel
        this.itemPosition = position
        setViews()
    }

    private fun setViews() {
        binding.tvTitle.text = itemOptions.title
        binding.rgDetails.removeAllViews()
        for (i in itemOptions.optionList.indices) {
            addRadioButton(itemOptions.optionList[i], i)
        }
    }

    private fun addRadioButton(option: Option, optionPosition: Int) {
        val itemRadioButton = OptionRadioButton(binding.root.context)

        itemRadioButton.init(viewModel, optionPosition) { position ->
            viewModel.setSelection(itemPosition, position)
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