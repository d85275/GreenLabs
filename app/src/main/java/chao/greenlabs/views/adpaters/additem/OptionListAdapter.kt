package chao.greenlabs.views.adpaters.additem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.databinding.ItemOptionListBinding
import chao.greenlabs.datamodels.Option
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.viewmodels.AddItemViewModel

class OptionListAdapter(
    private val viewModel: AddItemViewModel,
    private val categoryPosition: Int
) :
    RecyclerView.Adapter<OptionListViewHolder>() {

    private val optionList = arrayListOf<Option>()
    private lateinit var binding: ItemOptionListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionListViewHolder {
        binding =
            ItemOptionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionListViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    override fun onBindViewHolder(holder: OptionListViewHolder, position: Int) {
        holder.bindView(binding, optionList[position], categoryPosition, position, viewModel)
    }

    fun setList(list: List<Option>) {
        optionList.clear()
        optionList.addAll(list)

        notifyDataSetChanged()
    }
}

class OptionListViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

    private lateinit var binding: ItemOptionListBinding
    private lateinit var option: Option
    private lateinit var viewModel: AddItemViewModel
    private var context: Context? = null

    fun bindView(
        binding: ItemOptionListBinding,
        option: Option,
        categoryPosition: Int,
        position: Int,
        viewModel: AddItemViewModel
    ) {
        this.binding = binding
        this.option = option
        this.viewModel = viewModel
        context = binding.root.context
        setViews()
        setListeners(categoryPosition, position)
    }

    private fun setViews() {
        binding.tvOption.text = option.title
        binding.tvPrice.text = context!!.getString(R.string.add_price, option.addPrice)
    }

    private fun setListeners(categoryPosition: Int, position: Int) {
        binding.ivRemove.setOnClickListener {
            viewModel.removeOption(categoryPosition, position)
        }

        binding.tvOption.setOnClickListener {
            val default = binding.tvOption.text.toString()
            DialogUtils.showEditText(context!!, {
                viewModel.editOptionTitle(default, it, categoryPosition, position)
            }, default)
        }

        binding.tvPrice.setOnClickListener {
            val default = option.addPrice
            DialogUtils.showEditNumber(context!!, {
                viewModel.editOptionPrice(default, it, categoryPosition, position)
            }, default)
        }
    }
}