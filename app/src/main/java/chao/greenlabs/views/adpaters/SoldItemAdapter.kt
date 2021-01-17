package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.SoldData
import chao.greenlabs.viewmodels.ManageMarketViewModel
import kotlinx.android.synthetic.main.item_sold_items.view.*

class SoldItemAdapter(private val viewModel: ManageMarketViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = arrayListOf<SoldData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_sold_items, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val price = holder.itemView.context.getString(R.string.price, itemList[position].price)
        val bitmap = viewModel.getImage(itemList[position].name, itemList[position].price)
        holder.itemView.tv_name.text = itemList[position].name
        holder.itemView.tv_price.text = price
        holder.itemView.tv_count.text = itemList[position].count.toString()
        holder.itemView.iv_image.setImageBitmap(bitmap)
    }

    fun setItem(soldList: List<SoldData>) {
        itemList.clear()
        itemList.addAll(soldList)
        notifyDataSetChanged()
    }
}