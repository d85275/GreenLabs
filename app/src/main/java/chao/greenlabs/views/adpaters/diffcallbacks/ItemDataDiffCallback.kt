package chao.greenlabs.views.adpaters.diffcallbacks

import androidx.recyclerview.widget.DiffUtil
import chao.greenlabs.datamodels.ItemData

class ItemDataDiffCallback(
    private val oldData: List<ItemData>,
    private val newData: List<ItemData>
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
        val oldBitmap = oldData[oldItemPosition].getImage()
        val newBitmap = newData[newItemPosition].getImage()
        val theSame = oldBitmap?.sameAs(newBitmap)

        if (theSame != null) {
            return (oldData[oldItemPosition].name == newData[newItemPosition].name) &&
                    (oldData[oldItemPosition].price == newData[newItemPosition].price) &&
                    theSame
        }

        return (oldData[oldItemPosition].name == newData[newItemPosition].name) &&
                (oldData[oldItemPosition].price == newData[newItemPosition].price)
    }
}