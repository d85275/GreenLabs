package chao.market_helper.views.adpaters.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import chao.market_helper.datamodels.CustomerData

class CustomerDataDiffCallback(
    private val oldData: List<CustomerData>,
    private val newData: List<CustomerData>
) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].customerId == newData[newItemPosition].customerId
    }

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldData[oldItemPosition].customerId == newData[newItemPosition].customerId) &&
                (oldData[oldItemPosition].total == newData[newItemPosition].total) &&
                (oldData[oldItemPosition].marketId == newData[newItemPosition].marketId) &&
                (oldData[oldItemPosition].discount == newData[newItemPosition].discount) &&
                (oldData[oldItemPosition].memo == newData[newItemPosition].memo) &&
                (oldData[oldItemPosition].soldDataList?.size == newData[newItemPosition].soldDataList?.size)
    }
}