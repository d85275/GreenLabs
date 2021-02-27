package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.databinding.ItemOptionsBinding
import chao.greenlabs.datamodels.ItemOptions
import chao.greenlabs.viewmodels.ItemOptionsViewModel
import chao.greenlabs.views.viewholders.OptionViewHolder

class ItemOptionsAdapter(
    private val viewModel: ItemOptionsViewModel
) : RecyclerView.Adapter<OptionViewHolder>() {
    private val itemOptionsList = arrayListOf<ItemOptions>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOptionsBinding.inflate(inflater, parent, false)
        return OptionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemOptionsList.size
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bindView(itemOptionsList[position], position, viewModel)
    }

    fun setList(list: List<ItemOptions>) {
        itemOptionsList.clear()
        itemOptionsList.addAll(list)
        notifyDataSetChanged()
    }
}