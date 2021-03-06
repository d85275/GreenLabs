package chao.greenlabs.views.adpaters.addcustomer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.databinding.ItemOptionsBinding
import chao.greenlabs.datamodels.OptionCategory
import chao.greenlabs.viewmodels.ItemOptionsViewModel
import chao.greenlabs.views.viewholders.OptionViewHolder

class ItemOptionsAdapter(
    private val viewModel: ItemOptionsViewModel
) : RecyclerView.Adapter<OptionViewHolder>() {
    private val categoryList = arrayListOf<OptionCategory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOptionsBinding.inflate(inflater, parent, false)
        return OptionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bindView(categoryList[position], position, viewModel)
    }

    fun setList(list: List<OptionCategory>) {
        categoryList.clear()
        categoryList.addAll(list)
        notifyDataSetChanged()
    }
}