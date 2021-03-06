package chao.market_helper.views.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import chao.market_helper.R
import chao.market_helper.datamodels.MarketData
import kotlinx.android.synthetic.main.item_market.view.*

class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bindView(
        position: Int,
        marketData: MarketData,
        onClickedListener: ((data: MarketData) -> Unit)
    ) {
        val name = marketData.name
        val date = marketData.date
        val location = marketData.location
        val income = marketData.income
        val startTime = marketData.startTime
        val endTime = marketData.endTime
        itemView.tv_name.text = name
        itemView.tv_date.text = date
        itemView.tv_location.text = location
        itemView.tv_income.text = income
        itemView.tv_start.text = startTime
        itemView.tv_end.text = endTime
        itemView.setOnClickListener {
            onClickedListener.invoke(marketData)
        }
        setBackground(position)
    }

    private fun setBackground(position: Int) {
        when (position % 2) {
            0 -> {
                itemView.cl_parent.setBackgroundColor(itemView.context.getColor(R.color.market_list_bg_blue))
            }
            else -> {
                itemView.cl_parent.setBackgroundColor(itemView.context.getColor(R.color.market_list_bg_white))
            }
        }
    }
}