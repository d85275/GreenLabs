package chao.greenlabs.viewmodels

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.lang.Exception


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

    fun deleteUpdatedData() {
        val updatedItem = updatedItem.value ?: return
        repository.deleteItem(updatedItem).doOnComplete {
            msg.postValue("資料已刪除")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    fun onConfirmClicked(name: String, price: String, imageView: ImageView) {
        // save the image to file and keep the file name
        try {
            val bm = (imageView.drawable as BitmapDrawable).bitmap
            val fileName = StringBuilder().append(name).append("_").append(price).toString()
            repository.saveImageToExternal(fileName, bm)

        } catch (e: Exception) {
            Log.e("AddItemVM", "error: $e")
            msg.postValue("儲存圖片時發生錯誤")
            return
        }

        val data = ItemData(name, price)

        // create an item data and save it into our database

        val updatedItem = updatedItem.value
        when {
            updatedItem == null -> {
                repository.addItem(data).doOnComplete {
                    msg.postValue("品項已儲存")
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            }
            updatedItem.name == data.name -> {
                repository.updateSoldItemByName(updatedItem.name, data.name, data.price)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
                repository.updateItem(data).doOnComplete {
                    msg.postValue("品項已更新")
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            }
            else -> {
                repository.updateSoldItemByName(updatedItem.name, data.name, data.price)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
                repository.deleteItem(updatedItem).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
                repository.addItem(data).doOnComplete {
                    msg.postValue("品項已更新")
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            }
        }
    }

    fun getTmpPath(): File? {
        return repository.getTmpPath()
    }

    fun getImage(name: String, price: String): Bitmap {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getSavedImage(fileName)
    }
}