package chao.greenlabs.views.adpaters.additem

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.databinding.ItemAddOptionCategoryBinding
import chao.greenlabs.databinding.ItemOptionCategoryBinding
import chao.greenlabs.datamodels.OptionCategory
import chao.greenlabs.viewmodels.AddItemViewModel
import chao.greenlabs.views.viewholders.options.AddOptionCategoryViewHolder
import chao.greenlabs.views.viewholders.options.OptionCategoryViewHolder
import chao.greenlabs.views.viewholders.options.BaseViewHolder

private const val VIEW_TYPE_EMPTY = 0
private const val VIEW_TYPE_ADD = 1

class AddCategoryAdapter(
    private val viewModel: AddItemViewModel
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val categoryList = arrayListOf<OptionCategory?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_EMPTY) {
            val binding = ItemAddOptionCategoryBinding.inflate(inflater, parent, false)
            AddOptionCategoryViewHolder(binding, viewModel)
        } else {
            val binding = ItemOptionCategoryBinding.inflate(inflater, parent, false)
            OptionCategoryViewHolder(binding, viewModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (categoryList[position] == null) VIEW_TYPE_EMPTY else VIEW_TYPE_ADD
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindView(categoryList[position], position)
    }

    fun setList(list: List<OptionCategory?>) {
        Log.e("123","set list")
        categoryList.clear()
        categoryList.addAll(list)
        notifyDataSetChanged()
    }
}