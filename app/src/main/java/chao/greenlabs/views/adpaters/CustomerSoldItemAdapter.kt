package chao.greenlabs.views.adpaters

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.utils.BitmapUtils
import chao.greenlabs.views.adpaters.diffcallbacks.SoldItemDiffCallback
import kotlinx.android.synthetic.main.view_customer_item.view.iv_image
import kotlinx.android.synthetic.main.view_customer_item.view.tv_count
import kotlinx.android.synthetic.main.view_customer_item.view.tv_name
import kotlinx.android.synthetic.main.view_customer_item.view.tv_price
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private const val LOAD_IMAGE_DELAY_MS = 150L

class CustomerSoldItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

        holder.itemView.tv_name.text = name
        holder.itemView.tv_price.text = context.getString(R.string.price, price)
        holder.itemView.tv_count.text = count.toString()

        Handler(context.mainLooper).postDelayed({
            MainScope().launch {
                val bitmap = itemList[position].getImage()
                BitmapUtils.loadBitmap(context, bitmap, holder.itemView.iv_image)
            }
        }, LOAD_IMAGE_DELAY_MS)
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