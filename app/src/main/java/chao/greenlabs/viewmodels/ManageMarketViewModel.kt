package chao.greenlabs.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.datamodels.SoldData
import chao.greenlabs.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val TAG = "ManageMarketViewModel"

class ManageMarketViewModel(private val repository: Repository) : ViewModel() {

    init {
        loadItemData()
    }

    private val marketData = MutableLiveData<MarketData>()

    private val itemList = arrayListOf<ItemData>()
    private val matchedItems = MutableLiveData(arrayListOf<ItemData>())
    private val soldItems = MutableLiveData(arrayListOf<SoldData>())

    fun getMarketData(): LiveData<MarketData> = marketData

    fun setMarketData(data: MarketData) {
        marketData.value = data
    }

    fun getMatchedItems(): LiveData<ArrayList<ItemData>> = matchedItems

    fun getSoldItems(): LiveData<ArrayList<SoldData>> = soldItems

    private fun loadItemData() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.getItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e("MMVM", "e: $e") }.subscribe { list ->
                    itemList.addAll(list.reversed())
                }
        )
    }

    fun onSearch(text: String) {
        val list =
            itemList.filter { itemData ->
                itemData.name.contains(text) || itemData.price.contains(
                    text
                )
            }

        matchedItems.value = list as ArrayList<ItemData>
    }

    fun getImage(name: String, price: String): Bitmap {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getSavedImage(fileName)
    }

    fun onSearchItemClicked(itemData: ItemData) {
        val list: ArrayList<SoldData> = soldItems.value as ArrayList<SoldData>
        var isExist = false
        for (i in list.indices) {
            val soldData = list[i]
            if (soldData.name == itemData.name || soldData.price == itemData.price) {
                isExist = true
                soldData.count++
                list[i] = soldData
                break
            }
        }

        if (!isExist) {
            val name = itemData.name
            val price = itemData.price
            val marketData = this.marketData.value!!
            val soldData = SoldData.create(name, price, marketData)
            list.add(soldData)
        }

        soldItems.value = list
    }

}