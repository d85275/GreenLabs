package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.viewmodels.ItemListViewModel
import kotlinx.android.synthetic.main.item_items.view.*

class ItemAdapter(
    private val listViewModel: ItemListViewModel,
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
        val price = holder.itemView.context.getString(R.string.price, itemList[position].price)
        val bitmap = listViewModel.getImage(itemList[position].name, itemList[position].price)
        holder.itemView.tv_name.text = itemList[position].name
        holder.itemView.tv_price.text = price
        holder.itemView.iv_image.setImageBitmap(bitmap)
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