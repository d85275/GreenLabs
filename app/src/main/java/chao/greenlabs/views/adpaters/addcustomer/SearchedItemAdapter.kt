package chao.greenlabs.views.adpaters.addcustomer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.utils.ImageUtils
import kotlinx.android.synthetic.main.item_searched_items.view.*

class SearchedItemAdapter(
    private val onClickedListener: ((data: ItemData) -> Unit)
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = arrayListOf<ItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_searched_items, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val price = holder.itemView.context.getString(R.string.price, itemList[position].price)
        holder.itemView.tv_name.text = itemList[position].name
        holder.itemView.tv_price.text = price
        ImageUtils.loadImage(
            holder.itemView.context,
            itemList[position].name,
            holder.itemView.iv_image
        )
        holder.itemView.setOnClickListener { onClickedListener.invoke(itemList[position]) }
    }

    fun setList(items: List<ItemData>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }
}