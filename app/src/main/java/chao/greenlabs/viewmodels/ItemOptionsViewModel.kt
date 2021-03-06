package chao.greenlabs.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.OptionCategory
import chao.greenlabs.datamodels.Option
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.LogUtils

class ItemOptionsViewModel(private val repository: Repository) : ViewModel() {

    private var isOptionsSelected = MutableLiveData(false)
    private var totalPrice = MutableLiveData("")

    fun getIsOptionsSelected(): LiveData<Boolean> = isOptionsSelected

    fun getTotalPrice(): LiveData<String> = totalPrice

    fun clear() {
        isOptionsSelected = MutableLiveData(false)
    }

    private lateinit var itemData: ItemData

    fun getItemData(): ItemData = itemData

    fun getCategoryList(): List<OptionCategory> {
        return itemData.optionCategory.filterNot { it.title.isEmpty() && it.optionList.isEmpty() }
    }

    fun setItemData(itemData: ItemData) {
        this.itemData = itemData
        isOptionsSelected.value =
            itemData.optionCategory.filterNot { it.title.isEmpty() && it.optionList.isEmpty() }
                .isEmpty()
        totalPrice.value = itemData.price
    }

    fun loadBitmap(name: String, price: String): Bitmap? {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getSavedImage(fileName)
    }

    fun setSelection(categoryPosition: Int, optionPosition: Int) {
        updateSelection(categoryPosition, optionPosition)
        updateTotalPrice(categoryPosition, optionPosition)
        updateSelectionState()
    }

    private fun updateSelection(categoryPosition: Int, optionPosition: Int) {
        for (i in itemData.optionCategory[categoryPosition].optionList.indices) {
            itemData.optionCategory[categoryPosition].optionList[i].isSelected = i == optionPosition
        }
    }

    private fun updateTotalPrice(categoryPosition: Int, optionPosition: Int) {
        var totalAddedPrice = 0
        itemData.optionCategory.forEach { category ->
            val options = category.optionList.find { it.isSelected }
            totalAddedPrice += options?.addPrice?.toInt() ?: 0
        }
        val total = (itemData.price.toInt() + totalAddedPrice).toString()
        totalPrice.value = total
        itemData.price = total
    }

    private fun updateSelectionState() {
        itemData.optionCategory.forEach { category ->
            val notSelected = category.optionList.none { it.isSelected }
            if (notSelected) {
                isOptionsSelected.value = false
                return
            }
        }
        isOptionsSelected.value = true
    }

    fun save() {
        Log.e("123", "save data here")
    }
}