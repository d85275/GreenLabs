package chao.market_helper.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.market_helper.R
import chao.market_helper.datamodels.MarketData
import kotlinx.android.synthetic.main.item_add_market.view.*

class AddMarketAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var marketList = arrayListOf<MarketData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_market, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tv_date.text = marketList[position].date
        holder.itemView.tv_start.text = marketList[position].startTime
        holder.itemView.tv_end.text = marketList[position].endTime
        holder.itemView.tv_fee.text = marketList[position].fee
    }

    fun setList(items: List<MarketData>) {
        marketList.clear()
        marketList.addAll(items)
        notifyDataSetChanged()
    }
}