package chao.greenlabs.views.adpaters.addcustomer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.utils.ImageUtils
import chao.greenlabs.views.adpaters.diffcallbacks.SoldItemDiffCallback
import kotlinx.android.synthetic.main.item_customer_item.view.*


class CustomerSoldItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = arrayListOf<CustomerData.SoldItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_customer_item, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val soldItem = itemList[position]
        val name = soldItem.name
        val context = holder.itemView.context
        val price = soldItem.price
        val count = soldItem.count

        holder.itemView.tv_name.text = name
        holder.itemView.tv_options.text = getSelectedOptions(soldItem)
        holder.itemView.tv_price.text = context.getString(R.string.price, price)
        holder.itemView.tv_count.text = count.toString()

        ImageUtils.loadImage(context, name, holder.itemView.iv_image)
    }

    private fun getSelectedOptions(soldItem: CustomerData.SoldItem): String {
        val categories = soldItem.optionCategory ?: return ""
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

    fun setList(items: List<CustomerData.SoldItem>) {
        val diffCallback =
            SoldItemDiffCallback(itemList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}