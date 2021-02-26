package chao.greenlabs.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.ItemDetails

class ItemOptionsViewModel : ViewModel() {

    private val selections = arrayListOf<String>()
    private val options = arrayListOf<ItemDetails>()

    private var optionsData = MutableLiveData<List<ItemDetails>>()

    private var isOptionsSelected = MutableLiveData(false)

    fun getIsOptionsSelected(): LiveData<Boolean> = isOptionsSelected

    fun getOptionsData(): LiveData<List<ItemDetails>> = optionsData

    fun clear() {
        selections.clear()
        options.clear()
        optionsData = MutableLiveData()
        isOptionsSelected = MutableLiveData(false)
    }

    fun loadTestData() {
        Log.e("123", "set test data")
        options.add(ItemDetails("款式", arrayListOf("耳針", "耳夾")))
        options.add(ItemDetails("寶石", arrayListOf("白水晶", "粉晶", "珍珠")))
        options.add(ItemDetails("顏色", arrayListOf("白色", "粉色")))
        optionsData.value = options
        initSelections()
    }

    private fun initSelections() {
        for (i in 0 until options.size) {
            selections.add("")
        }
    }

    fun setSelection(position: Int, selectValue: String) {
        Log.e("123", "set data $position with value: $selectValue")
        selections[position] = selectValue
        Log.e("123", "check ${selections.none { it == "" }}")
        isOptionsSelected.value = selections.none { it == "" }
    }
}