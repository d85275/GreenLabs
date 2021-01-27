package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.views.MarketViewHolder
import kotlinx.android.synthetic.main.item_market.view.*
import kotlinx.android.synthetic.main.item_market.view.tv_name

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