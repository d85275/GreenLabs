package chao.greenlabs.views.viewholders.options

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.databinding.ItemOptionCategoryBinding
import chao.greenlabs.datamodels.OptionCategory
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.viewmodels.AddItemViewModel
import chao.greenlabs.views.adpaters.item.OptionListAdapter

class OptionCategoryViewHolder(
    private val binding: ItemOptionCategoryBinding,
    private val viewModel: AddItemViewModel
) : BaseViewHolder(binding.root) {

    private lateinit var optionCategory: OptionCategory
    private lateinit var adapter: OptionListAdapter

    private var mPosition = -1
    private var context: Context = binding.root.context

    override fun bindView(optionCategory: OptionCategory?, position: Int) {
        super.bindView(optionCategory, position)
        this.optionCategory = optionCategory ?: return
        mPosition = position
        setViews()
        setListener()
    }

    private fun setViews() {
        binding.tvTitle.text = optionCategory.title

        adapter = OptionListAdapter(viewModel, mPosition)
        val layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvOptionList.layoutManager = layoutManager
        binding.rvOptionList.setHasFixedSize(true)
        binding.rvOptionList.adapter = adapter
        adapter.setList(optionCategory.optionList)
    }

    private fun setListener() {
        val text =
            if (binding.tvTitle.text.toString().trim().isEmpty()) context.getString(R.string.title)
            else binding.tvTitle.text.toString().trim()
        binding.tvTitle.setOnClickListener {
            val default = optionCategory.title
            DialogUtils.showEditText(context, {
                viewModel.editCategoryTitle(mPosition, default, it)
            }, text)
        }

        binding.llAdd.setOnClickListener {
            addOption()
        }
    }

    private fun addOption() {
        val defaultTitle = binding.root.context.getString(R.string.added_option)
        viewModel.addOption(defaultTitle, mPosition)

    }
}