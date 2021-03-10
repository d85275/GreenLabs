package chao.greenlabs.views.adpaters.addcustomer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.utils.ImageUtils
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.viewmodels.AddCustomerViewModel
import chao.greenlabs.views.adpaters.diffcallbacks.SoldItemDiffCallback
import kotlinx.android.synthetic.main.item_sold_items.view.*

class SoldItemAdapter(
    private val viewModel: AddCustomerViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = arrayListOf<CustomerData.SoldItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sold_items, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val price = holder.itemView.context.getString(R.string.price, itemList[position].price)
        holder.itemView.tv_name.text = itemList[position].name
        holder.itemView.tv_options.text = getSelectedOptions(itemList[position])
        holder.itemView.tv_price.text = price
        holder.itemView.tv_count.text = itemList[position].count.toString()
        ImageUtils.loadImage(context, itemList[position].name, holder.itemView.iv_image)
        setListeners(holder)
    }

    private fun getSelectedOptions(soldItem: CustomerData.SoldItem): String {
        val categories = soldItem.optionCategory
        if (categories.isEmpty()) return ""
        val options = StringBuilder()
        categories.forEach { category ->
            if (category.title.isNotEmpty()) {
                category.optionList.forEach { option ->
                    if (option.isSelected) options.append(option.title).append(" ")
                }
            }
        }

        return options.toString()
    }

    private fun setListeners(holder: RecyclerView.ViewHolder) {

        holder.itemView.ll_minus.setOnClickListener {
            if (itemList[holder.adapterPosition].count == 1) {
                val confirmAction: (() -> Unit) = {
                    viewModel.deleteSoldItem(holder.adapterPosition)
                }
                DialogUtils.showDelete(holder.itemView.context, confirmAction)
            } else {
                viewModel.updateCount(holder.adapterPosition, -1)
            }
        }

        holder.itemView.ll_plus.setOnClickListener {
            viewModel.updateCount(holder.adapterPosition, 1)
        }
    }

    fun setItem(soldList: List<CustomerData.SoldItem>) {
        val diffCallback =
            SoldItemDiffCallback(
                itemList,
                soldList
            )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemList.clear()
        itemList.addAll(soldList)
        diffResult.dispatchUpdatesTo(this)
    }
}