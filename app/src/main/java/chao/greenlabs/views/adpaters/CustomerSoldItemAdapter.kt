package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.utils.BitmapUtils
import chao.greenlabs.viewmodels.ManageMarketViewModel
import kotlinx.android.synthetic.main.view_customer_item.view.iv_image
import kotlinx.android.synthetic.main.view_customer_item.view.tv_count
import kotlinx.android.synthetic.main.view_customer_item.view.tv_name
import kotlinx.android.synthetic.main.view_customer_item.view.tv_price

class CustomerSoldItemAdapter(private val viewModel: ManageMarketViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = arrayListOf<CustomerData.SoldItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_customer_item, parent, false)
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
        val bitmap = viewModel.getImage(name, price)

        holder.itemView.tv_name.text = name
        holder.itemView.tv_price.text = context.getString(R.string.price, price)
        holder.itemView.tv_count.text = count.toString()
        BitmapUtils.loadIntoView(context, bitmap, holder.itemView.iv_image)
    }

    fun setList(items: List<CustomerData.SoldItem>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }
}