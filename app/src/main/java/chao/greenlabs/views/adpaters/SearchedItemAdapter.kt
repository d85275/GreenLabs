package chao.greenlabs.views.adpaters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.utils.BitmapUtils
import chao.greenlabs.viewmodels.AddCustomerViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.item_searched_items.view.*

class SearchedItemAdapter(
    private val viewModel: AddCustomerViewModel,
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
        val bitmap = viewModel.getImage(itemList[position].name, itemList[position].price)
            ?: getDefaultBitmap(holder.itemView.context)
        holder.itemView.tv_name.text = itemList[position].name
        holder.itemView.tv_price.text = price
        BitmapUtils.loadIntoView(holder.itemView.context, bitmap, holder.itemView.iv_image)
        holder.itemView.setOnClickListener { onClickedListener.invoke(itemList[position]) }
    }

    private fun getDefaultBitmap(context: Context): Bitmap? {
        return AppCompatResources.getDrawable(context, R.mipmap.ic_launcher)?.toBitmap()
    }

    fun setList(items: List<ItemData>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }
}