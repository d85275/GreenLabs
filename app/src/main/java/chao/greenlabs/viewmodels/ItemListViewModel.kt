package chao.greenlabs.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val TAG = "ManageItemViewModel"

class ItemListViewModel(private val repository: Repository) : ViewModel() {

    private val itemList = MutableLiveData<List<ItemData>>()

    fun getItemList(): LiveData<List<ItemData>> = itemList

    fun loadItemData() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.getItems().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).doOnError { e -> Log.e(TAG, "Error when loading saved items: $e") }
                .subscribe { list ->
                    itemList.postValue(list)
                }
        )
    }

    fun getImage(name: String, price: String): Bitmap {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getSavedImage(fileName)
    }
}