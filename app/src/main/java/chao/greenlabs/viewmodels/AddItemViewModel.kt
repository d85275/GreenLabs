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

    fun onConfirmClicked(name: String, price: String, imageView: ImageView) {
        if (name.isEmpty() || price.isEmpty()) return

        // save the image to file and keep the file name
        try {
            val bm = (imageView.drawable as BitmapDrawable).bitmap
            val fileName = StringBuilder().append(name).append("_").append(price).toString()
            repository.saveImageToExternal(fileName, bm)

        } catch (e: Exception) {
            msg.postValue("Error when saving the item image")
            return
        }

        val data = ItemData(name, price)

        // create an item data and save it into our database

        val updatedItem = updatedItem.value
        when {
            updatedItem == null -> {
                repository.addItem(data).doOnComplete {
                    msg.postValue("Item is saved")
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            }
            updatedItem.name == data.name -> {
                repository.updateSoldItemByName(updatedItem.name, data.name, data.price)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
                repository.updateItem(data).doOnComplete {
                    msg.postValue("Item is updated")
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            }
            else -> {
                repository.updateSoldItemByName(updatedItem.name, data.name, data.price)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
                repository.deleteItem(updatedItem).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe()
                repository.addItem(data).doOnComplete {
                    msg.postValue("Item is changed")
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