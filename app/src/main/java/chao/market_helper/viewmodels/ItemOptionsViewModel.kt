package chao.market_helper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.market_helper.datamodels.ItemData
import chao.market_helper.datamodels.OptionCategory

class ItemOptionsViewModel : ViewModel() {

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

    fun setSelection(categoryPosition: Int, optionPosition: Int) {
        updateSelection(categoryPosition, optionPosition)
        totalPrice.value = getPrice()
        updateSelectionState()
    }

    private fun updateSelection(categoryPosition: Int, optionPosition: Int) {
        for (i in itemData.optionCategory[categoryPosition].optionList.indices) {
            itemData.optionCategory[categoryPosition].optionList[i].isSelected = i == optionPosition
        }
    }

    private fun getPrice(): String {
        var totalAddedPrice = 0
        itemData.optionCategory.forEach { category ->
            val options = category.optionList.find { it.isSelected }
            totalAddedPrice += options?.addPrice?.toInt() ?: 0
        }
        return (itemData.price.toInt() + totalAddedPrice).toString()
    }

    fun getSavedItem(): ItemData {
        itemData.price = getPrice()
        return itemData
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
}