package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.utils.BitmapUtils
import chao.greenlabs.viewmodels.ItemListViewModel
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
        val price = context.getString(R.string.price, itemList[position].price)
        holder.itemView.tv_name.text = itemList[position].name
        holder.itemView.tv_price.text = price
        val bitmap = itemList[position].getImage()
        BitmapUtils.loadBitmap(context, bitmap, holder.itemView.iv_image)
        holder.itemView.setOnClickListener {
            onItemClickedAction.invoke(itemList[position])
        }
    }

    fun setList(items: List<ItemData>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }
}