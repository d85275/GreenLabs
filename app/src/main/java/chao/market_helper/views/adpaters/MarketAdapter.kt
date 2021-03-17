package chao.market_helper.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.market_helper.R
import chao.market_helper.datamodels.MarketData
import chao.market_helper.views.viewholders.MarketViewHolder

class MarketAdapter(private val onClickedListener: ((data: MarketData) -> Unit)) :
    RecyclerView.Adapter<MarketViewHolder>() {

    private var marketList = arrayListOf<MarketData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_market, parent, false)
        return MarketViewHolder(view)
    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bindView(position, marketList[position], onClickedListener)
    }

    fun setList(items: List<MarketData>) {
        marketList.clear()
        marketList.addAll(items)
        notifyDataSetChanged()
    }
}