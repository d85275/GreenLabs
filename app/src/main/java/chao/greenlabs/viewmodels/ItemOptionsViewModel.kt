package chao.greenlabs.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.OptionCategory
import chao.greenlabs.datamodels.Option

class ItemOptionsViewModel : ViewModel() {

    //private val categoryList = arrayListOf<OptionCategory>()

    private var isOptionsSelected = MutableLiveData(false)

    fun getIsOptionsSelected(): LiveData<Boolean> = isOptionsSelected

    fun clear() {
        //categoryList.clear()
        isOptionsSelected = MutableLiveData(false)
    }

    private lateinit var itemData: ItemData

    fun getItemData(): ItemData = itemData

    fun setItemData(itemData: ItemData) {
        this.itemData = itemData
    }

    /*
    suspend fun loadData(): List<OptionCategory> {
        categoryList.add(OptionCategory("款式", arrayListOf(Option("耳針", "0"), Option("耳夾", "50"))))
        categoryList.add(
            OptionCategory(
                "寶石",
                arrayListOf(Option("白水晶", "10"), Option("珍珠", "20"), Option("粉水晶", "40"))
            )
        )
        categoryList.add(OptionCategory("顏色", arrayListOf(Option("粉色", "0"), Option("白色", "0"))))
        return categoryList

    }
     */

    fun setSelection(position: Int, optionPosition: Int) {
        for (i in itemData.optionCategory[position].optionList.indices) {
            itemData.optionCategory[position].optionList[i].isSelected = i == optionPosition
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