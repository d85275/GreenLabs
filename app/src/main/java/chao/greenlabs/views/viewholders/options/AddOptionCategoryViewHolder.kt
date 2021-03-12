package chao.greenlabs.views.viewholders.options

import chao.greenlabs.R
import chao.greenlabs.databinding.ItemAddOptionCategoryBinding
import chao.greenlabs.datamodels.OptionCategory
import chao.greenlabs.viewmodels.AddItemViewModel

class AddOptionCategoryViewHolder(
    private val binding: ItemAddOptionCategoryBinding,
    private val viewModel: AddItemViewModel
) : BaseViewHolder(binding.root) {


    override fun bindView(optionCategory: OptionCategory?, position: Int) {
        super.bindView(optionCategory, position)
        setListeners()
    }

    private fun setListeners() {
        binding.root.setOnClickListener {
            val context = binding.root.context
            val defaultTitle = context.getString(R.string.added_category)
            viewModel.addCategory(defaultTitle)
        }
    }
}