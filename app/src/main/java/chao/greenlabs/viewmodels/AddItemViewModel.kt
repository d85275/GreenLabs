package chao.greenlabs.viewmodels

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
    private val isSavingFinished = MutableLiveData(false)

    fun getMessage(): LiveData<String> = msg
    fun getIsSavingFinished(): LiveData<Boolean> = isSavingFinished

    fun addItem(name: String, price: String, imageView: ImageView) {
        if (name.isEmpty() || price.isEmpty()) return
        Log.e("123", "price: $price")

        // save the image to file and keep the file name
        val bm = (imageView.drawable as BitmapDrawable).bitmap
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        try {
            repository.saveImageToExternal(fileName, bm)

        } catch (e: Exception) {
            msg.postValue("Error when saving the item image")
            return
        }

        val data = ItemData.create(name, price)

        isSavingFinished.postValue(true)
        // create an item data and save it into our database

        repository.addItem(data).doOnComplete {
            Log.e("123", "the data is saved")
            msg.postValue("Item is saved")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    fun getTmpPath(): File? {
        return repository.getTmpPath()
    }

}