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

class ItemOptionsViewModel(private val repository: Repository) : ViewModel() {

    private var isOptionsSelected = MutableLiveData(false)

    fun getIsOptionsSelected(): LiveData<Boolean> = isOptionsSelected

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
    }

    fun loadBitmap(name: String, price: String): Bitmap? {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getSavedImage(fileName)
    }

    fun setSelection(categoryPosition: Int, optionPosition: Int) {
        for (i in itemData.optionCategory[categoryPosition].optionList.indices) {
            itemData.optionCategory[categoryPosition].optionList[i].isSelected = i == optionPosition
        }

        itemData.optionCategory.forEach { option ->
            val notSelected = option.optionList.none { it.isSelected }
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