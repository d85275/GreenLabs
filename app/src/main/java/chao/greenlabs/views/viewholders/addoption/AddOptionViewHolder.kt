package chao.greenlabs.views.viewholders.addoption

import android.content.Context
import chao.greenlabs.R
import chao.greenlabs.databinding.ItemAddOptionsBinding
import chao.greenlabs.datamodels.Option
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.viewmodels.AddItemViewModel

class AddOptionViewHolder(
    private val binding: ItemAddOptionsBinding,
    private val viewModel: AddItemViewModel
) : BaseViewHolder(binding.root) {

    private lateinit var option: Option
    private var context: Context = binding.root.context

    override fun bindView(option: Option?) {
        super.bindView(option)
        this.option = option ?: return

        setViews()
        setListener()
    }

    private fun setViews() {
        if (option.title.isNotEmpty()) binding.tvTitle.text = option.title
    }

    private fun setListener() {
        val text =
            if (binding.tvTitle.text.toString().trim().isEmpty()) context.getString(R.string.title)
            else binding.tvTitle.text.toString().trim()
        binding.tvTitle.setOnClickListener {
            DialogUtils.showEditText(context, { enteredText ->
                if (enteredText.trim().isEmpty()) {
                    binding.tvTitle.text = context.getString(R.string.title)
                } else {
                    binding.tvTitle.text = enteredText.trim()
                }
            }, text)
        }

        binding.llAdd.setOnClickListener {
            addOption()
        }
    }

    private fun addOption() {
        
    }
}