package chao.greenlabs.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val TAG = "ManageMarketViewModel"

class ManageMarketViewModel(private val repository: Repository) : ViewModel() {

    init {
        loadItemData()
    }

    private val marketName = MutableLiveData<String>()
    private val marketDate = MutableLiveData<String>()

    private val itemList = arrayListOf<ItemData>()
    private val matchedItems = MutableLiveData<List<ItemData>>()

    fun getMarketName(): LiveData<String> = marketName
    fun getMarketDate(): LiveData<String> = marketDate

    fun setMarketData(name: String, date: String) {
        marketName.value = name
        marketDate.value = date
    }

    fun getMatchedItems(): LiveData<List<ItemData>> = matchedItems

    private fun loadItemData() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.getItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e("MMVM", "e: $e") }.subscribe { list ->
                    itemList.addAll(list)
                }
        )
    }

    fun onSearch(text: String) {
        val list =
            itemList.filter { itemData -> itemData.name.contains(text) || itemData.price.contains(text) }
        matchedItems.value = list
    }

    fun getImage(name: String, price: String): Bitmap {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getSavedImage(fileName)
    }

}