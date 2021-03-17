package chao.market_helper.views.adpaters.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import chao.market_helper.datamodels.CustomerData

class SoldItemDiffCallback(
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
                (oldData[oldItemPosition].count == newData[newItemPosition].count) &&
                (oldData[oldItemPosition].optionCategory.containsAll(newData[newItemPosition].optionCategory)) &&
                (newData[newItemPosition].optionCategory.containsAll(oldData[oldItemPosition].optionCategory))
    }
}