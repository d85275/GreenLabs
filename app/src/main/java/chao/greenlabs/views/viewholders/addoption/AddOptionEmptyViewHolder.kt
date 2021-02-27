package chao.greenlabs.views.viewholders.addoption

import chao.greenlabs.databinding.ItemAddOptionsEmptyBinding
import chao.greenlabs.datamodels.Option
import chao.greenlabs.viewmodels.AddItemViewModel

class AddOptionEmptyViewHolder(
    private val binding: ItemAddOptionsEmptyBinding,
    private val viewModel: AddItemViewModel
) : BaseViewHolder(binding.root) {

    override fun bindView(option: Option?) {
        super.bindView(option)
        setListeners()
    }

    private fun setListeners() {
        binding.root.setOnClickListener {
            viewModel.onAddOptionClicked()
        }
    }
}