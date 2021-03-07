package chao.greenlabs.views.adpaters.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.utils.ImageUtils
import chao.greenlabs.views.adpaters.diffcallbacks.ItemDataDiffCallback
import kotlinx.android.synthetic.main.item_items.view.*

class ItemAdapter(
    private val onItemClickedAction: ((itemData: ItemData) -> Unit)
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = arrayListOf<ItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_items, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val price = context.getString(R.string.price, itemList[holder.adapterPosition].price)
        holder.itemView.tv_name.text = itemList[holder.adapterPosition].name
        holder.itemView.tv_price.text = price
        ImageUtils.loadImage(context, itemList[holder.adapterPosition].name, holder.itemView.iv_image)
        holder.itemView.setOnClickListener {
            onItemClickedAction.invoke(itemList[holder.adapterPosition])
        }
    }

    fun setList(items: List<ItemData>) {
        val diffCallback = ItemDataDiffCallback(itemList, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemList.clear()
        itemList.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}