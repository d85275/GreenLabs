package chao.greenlabs.views.adpaters

import androidx.recyclerview.widget.DiffUtil
import chao.greenlabs.datamodels.CustomerData

class CustomerDataDiffCallback(
    private val oldData: List<CustomerData.SoldItem>,
    private val newData: List<CustomerData.SoldItem>
) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].name == newData[newItemPosition].name
    }

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldData[oldItemPosition].name == newData[newItemPosition].name) &&
                (oldData[oldItemPosition].price == newData[newItemPosition].price) &&
                (oldData[oldItemPosition].count == newData[newItemPosition].count)
    }
}