package chao.greenlabs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.ItemData

class AddCustomerSoldItemViewModel : ViewModel() {
    private val clickedItem = MutableLiveData<ItemData>()

    fun getClickedItem(): LiveData<ItemData> = clickedItem

    fun setClickedItem(item: ItemData) {
        clickedItem.value = item
    }
}