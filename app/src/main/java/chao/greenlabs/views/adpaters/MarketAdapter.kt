package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.MarketData
import kotlinx.android.synthetic.main.item_items.view.*
import kotlinx.android.synthetic.main.item_market.view.*
import kotlinx.android.synthetic.main.item_market.view.tv_name

class MarketAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var marketList = arrayListOf<MarketData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_market, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tv_name.text = marketList[position].name
        holder.itemView.tv_date.text = marketList[position].date
    }

    fun setList(items: List<MarketData>) {
        marketList.clear()
        marketList.addAll(items)
        notifyDataSetChanged()
    }
}