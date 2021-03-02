package chao.greenlabs.viewmodels

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.OptionCategory
import chao.greenlabs.datamodels.Option
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.BitmapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class AddItemViewModel(private val repository: Repository) : ViewModel() {

    private val msg = MutableLiveData<String>()
    private val updatedItem = MutableLiveData<ItemData>()

    private val itemOptions = MutableLiveData<List<OptionCategory?>>()

    private var isUpdateMode = false

    private var bitmapUpdated: Boolean = false

    fun bitmapUpdated(state: Boolean) {
        bitmapUpdated = state
    }

    fun getOptions(): LiveData<List<OptionCategory?>> = itemOptions

    fun loadOptions() {
        val list = arrayListOf<OptionCategory?>()
        list.add(null)
        itemOptions.value = list
    }


    fun editOptionTitle(
        default: String,
        value: String,
        categoryPosition: Int,
        optionPosition: Int
    ) {
        if (categoryPosition < 0) return
        itemOptions.value ?: return
        val options = itemOptions.value!!
        options[categoryPosition]?.optionList?.get(optionPosition)?.title =
            if (value.trim().isEmpty()) {
                default
            } else {
                value
            }
        itemOptions.value = options
    }

    fun editOptionPrice(
        default: String,
        value: String,
        categoryPosition: Int,
        optionPosition: Int
    ) {
        if (categoryPosition < 0) return
        itemOptions.value ?: return
        val options = itemOptions.value!!
        options[categoryPosition]?.optionList?.get(optionPosition)?.addPrice =
            if (value.trim().isEmpty()) {
                default
            } else {
                value
            }
        itemOptions.value = options
    }

    fun removeOption(categoryPosition: Int, optionPosition: Int) {
        if (categoryPosition < 0) return
        itemOptions.value ?: return
        val options = itemOptions.value!!
        options[categoryPosition]?.optionList?.removeAt(optionPosition)
        itemOptions.value = options
    }

    fun addOption(defaultTitle: String, categoryPosition: Int) {
        if (categoryPosition < 0) return
        itemOptions.value ?: return
        val options = itemOptions.value!!
        options[categoryPosition]?.optionList?.add(Option(defaultTitle, "0"))
        itemOptions.value = options
    }

    fun addCategory(defaultTitle: String) {
        itemOptions.value ?: return
        val options = itemOptions.value!!.filterNotNull()
        val list: ArrayList<OptionCategory?> = arrayListOf()
        list.addAll(options)
        list.add(OptionCategory(defaultTitle, arrayListOf()))
        list.add(null)
        itemOptions.value = list
    }

    fun editCategoryTitle(categoryPosition: Int, default: String, value: String) {
        if (categoryPosition < 0) return
        val options = itemOptions.value ?: return
        options[categoryPosition]?.title =
            if (value.trim().isEmpty()) {
                default
            } else {
                value
            }
        itemOptions.value = options
    }

    fun getMessage(): LiveData<String> = msg

    fun getUpdatedItem(): LiveData<ItemData> = updatedItem

    fun getIsUpdateMode(): Boolean {
        return isUpdateMode
    }

    fun setUpdatedItem(itemData: ItemData) {
        updatedItem.value = itemData
        isUpdateMode = true
    }

    fun clearUpdatedItem() {
        updatedItem.value = null
        isUpdateMode = false
        msg.value = ""
        bitmapUpdated(false)
    }

    private fun saveBitmap(name: String, price: String, imageView: ImageView) {
        if (!bitmapUpdated) return

        val bitmap = BitmapUtils.getBitmapFromImageView(imageView)
        try {
            val fileName = StringBuilder().append(name).append("_").append(price).toString()
            repository.saveImageToExternal(fileName, bitmap)

        } catch (e: Exception) {
            Log.e("AddItemVM", "error: $e")
            msg.postValue("儲存圖片時發生錯誤")
            return
        }
    }

    fun onConfirmClicked(name: String, price: String, imageView: ImageView) {

        saveBitmap(name, price, imageView)

        val data = ItemData(name, price)
        val updatedItem = updatedItem.value

        viewModelScope.launch(Dispatchers.IO) {
            when {
                updatedItem == null -> {
                    repository.addItem(data)
                    msg.postValue("品項已儲存")
                }

                updatedItem.name == data.name -> {
                    repository.updateItem(data)
                    msg.postValue("品項已更新")
                }

                else -> {
                    repository.deleteItem(updatedItem)
                    val fileName = StringBuilder().append(name).append("_").append(price).toString()
                    repository.deleteImage(fileName)
                    repository.addItem(data)
                    msg.postValue("品項已更新")
                }
            }
        }
    }

    fun getTmpPath(): File? {
        return repository.getTmpPath()
    }
}