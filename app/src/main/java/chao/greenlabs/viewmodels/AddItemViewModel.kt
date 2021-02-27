package chao.greenlabs.viewmodels

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.Option
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.BitmapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class AddItemViewModel(private val repository: Repository) : ViewModel() {

    private val msg = MutableLiveData<String>()
    private val updatedItem = MutableLiveData<ItemData>()

    private val options = MutableLiveData<List<Option?>>()

    private var isUpdateMode = false

    private var bitmapUpdated: Boolean = false

    fun bitmapUpdated(state: Boolean) {
        bitmapUpdated = state
    }

    fun getOptions(): LiveData<List<Option?>> = options

    fun loadOptions() {
        val list = arrayListOf<Option?>()
        list.add(null)
        options.value = list
    }

    fun onAddOptionClicked() {
        options.value ?: return
        val optionList = options.value!!.filterNotNull()
        val list:ArrayList<Option?> = arrayListOf()
        list.addAll(optionList)
        list.add(Option("", ""))
        list.add(null)
        options.value = list
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