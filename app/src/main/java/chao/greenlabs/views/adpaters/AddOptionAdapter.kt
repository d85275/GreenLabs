package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.databinding.ItemAddOptionsBinding
import chao.greenlabs.databinding.ItemAddOptionsEmptyBinding
import chao.greenlabs.datamodels.Option
import chao.greenlabs.viewmodels.AddItemViewModel
import chao.greenlabs.views.viewholders.addoption.AddOptionEmptyViewHolder
import chao.greenlabs.views.viewholders.addoption.AddOptionViewHolder
import chao.greenlabs.views.viewholders.addoption.BaseViewHolder

private const val VIEW_TYPE_EMPTY = 0
private const val VIEW_TYPE_ADD = 1

class AddOptionAdapter(
    private val viewModel: AddItemViewModel
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val optionList = arrayListOf<Option?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_EMPTY) {
            val binding = ItemAddOptionsEmptyBinding.inflate(inflater, parent, false)
            AddOptionEmptyViewHolder(binding, viewModel)
        } else {
            val binding = ItemAddOptionsBinding.inflate(inflater, parent, false)
            AddOptionViewHolder(binding, viewModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (optionList[position] == null) VIEW_TYPE_EMPTY else VIEW_TYPE_ADD
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindView(optionList[position])
    }

    fun setList(list: List<Option?>) {
        optionList.clear()
        optionList.addAll(list)
        notifyDataSetChanged()
    }
}