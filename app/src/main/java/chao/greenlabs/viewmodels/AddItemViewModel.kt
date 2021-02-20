package chao.greenlabs.viewmodels

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.BitmapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class AddItemViewModel(private val repository: Repository) : ViewModel() {

    private val msg = MutableLiveData<String>()
    private val updatedItem = MutableLiveData<ItemData>()

    private var isUpdateMode = false

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
    }

    fun onConfirmClicked(name: String, price: String, imageView: ImageView) {
        // save the image to file and keep the file name
        try {

            val result = BitmapUtils.getBitmapFromImageView(imageView)
            //val original = (imageView.drawable as BitmapDrawable).bitmap

            val fileName = StringBuilder().append(name).append("_").append(price).toString()
            repository.saveImageToExternal(fileName, result)

        } catch (e: Exception) {
            Log.e("AddItemVM", "error: $e")
            msg.postValue("儲存圖片時發生錯誤")
            return
        }

        val data = ItemData(name, price)

        // create an item data and save it into our database
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

    fun getImage(name: String, price: String): Bitmap? {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getTestSavedImage(fileName)
    }
}