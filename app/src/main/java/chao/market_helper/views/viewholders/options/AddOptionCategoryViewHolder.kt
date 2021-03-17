package chao.market_helper.views.viewholders.options

import chao.market_helper.R
import chao.market_helper.databinding.ItemAddOptionCategoryBinding
import chao.market_helper.datamodels.OptionCategory
import chao.market_helper.viewmodels.AddItemViewModel

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