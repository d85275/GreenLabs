package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.utils.BitmapUtils
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.viewmodels.AddCustomerViewModel
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
        val bitmap = viewModel.getImage(itemList[position].name, itemList[position].price)
        holder.itemView.tv_name.text = itemList[position].name
        holder.itemView.tv_price.text = price
        holder.itemView.tv_count.text = itemList[position].count.toString()
        BitmapUtils.loadBitmap(context, bitmap, holder.itemView.iv_image)
        setListeners(holder)
    }

    private fun setListeners(holder: RecyclerView.ViewHolder) {

        holder.itemView.ll_minus.setOnClickListener {
            if (itemList[holder.adapterPosition].count  == 1) {
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
        val diffCallback = SoldItemDiffCallback(itemList, soldList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemList.clear()
        itemList.addAll(soldList)
        diffResult.dispatchUpdatesTo(this)
    }
}