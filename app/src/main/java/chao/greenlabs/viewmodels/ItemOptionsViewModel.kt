package chao.greenlabs.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.ItemOptions
import chao.greenlabs.datamodels.Option

class ItemOptionsViewModel : ViewModel() {

    private val options = arrayListOf<ItemOptions>()

    private var isOptionsSelected = MutableLiveData(false)

    fun getIsOptionsSelected(): LiveData<Boolean> = isOptionsSelected

    fun clear() {
        options.clear()
        isOptionsSelected = MutableLiveData(false)
    }

    suspend fun loadData(): List<ItemOptions> {
        options.add(ItemOptions("款式", arrayListOf(Option("耳針", "0"), Option("耳夾", "50"))))
        options.add(
            ItemOptions(
                "寶石",
                arrayListOf(Option("白水晶", "10"), Option("珍珠", "20"), Option("粉水晶", "40"))
            )
        )
        options.add(ItemOptions("顏色", arrayListOf(Option("粉色", "0"), Option("白色", "0"))))
        return options

    }

    fun setSelection(position: Int, optionPosition: Int) {
        for (i in options[position].optionList.indices) {
            options[position].optionList[i].isSelected = i == optionPosition
        }

        options.forEach { option ->
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