package chao.greenlabs.viewmodels

import android.annotation.SuppressLint
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

    private val marketData = MutableLiveData<MarketData>()

    private val itemList = arrayListOf<ItemData>()
    private val matchedItems = MutableLiveData(arrayListOf<ItemData>())
    private val marketSoldItems = MutableLiveData(arrayListOf<SoldData>())
    private val allSoldPrice = MutableLiveData(0)

    private var totalPrice = 0

    fun getMarketData(): LiveData<MarketData> = marketData

    fun setMarketData(data: MarketData) {
        marketData.value = data
    }

    fun getMatchedItems(): LiveData<ArrayList<ItemData>> = matchedItems

    fun getMarketSoldItems(): LiveData<ArrayList<SoldData>> = marketSoldItems

    fun getAllSoldPrice(): LiveData<Int> = allSoldPrice

    var compositeDisposable = CompositeDisposable()


    fun loadItemData() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.getItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e(TAG, "e: $e") }.subscribe { list ->
                    itemList.clear()
                    itemList.addAll(list.reversed())
                }
        )
    }

    fun addAllSoldPrice(price: Int) {
        allSoldPrice.value = allSoldPrice.value!! + price
    }

    fun loadAllSoldPrice() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.getSoldItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e(TAG, "e: $e") }.subscribe { list ->
                    var total = 0
                    list.forEach { soldData ->
                        total += (soldData.price.toInt() * soldData.count)
                    }
                    allSoldPrice.value = allSoldPrice.value!! + total
                }
        )
    }

    fun loadMarketSoldData() {
        val marketData = this.marketData.value ?: return
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.getSoldItems(marketData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e(TAG, "e: $e") }.subscribe { list ->
                    marketSoldItems.postValue(list as ArrayList<SoldData>?)
                    Log.e("123", "sold list size: ${list.size}")
                }
        )
    }

    fun clearMarketSoldData() {
        marketSoldItems.postValue(arrayListOf())
        allSoldPrice.value = 0
        //compositeDisposable.dispose()
    }

    fun getTotalPrice(list: List<SoldData>): Int {
        val marketData = marketData.value ?: return 0
        var total = 0 - marketData.price.toInt()
        list.forEach { soldItem ->
            total += (soldItem.price.toInt() * soldItem.count)
        }
        totalPrice = total
        return total
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

    fun deleteSoldItem(position: Int) {
        val list = marketSoldItems.value ?: return
        val item = list[position]
        list.removeAt(position)
        val disposable = repository.deleteSoldItem(item).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                marketSoldItems.value = list
            }, {
                Log.e("123", "error: $it")
            })
        compositeDisposable.add(disposable)
    }

    fun updateCount(position: Int, count: Int) {
        val list = marketSoldItems.value ?: return
        val item = list[position]
        item.count += count
        val disposable = repository.updateSoldItem(item).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    marketSoldItems.value = list
                }, {
                    Log.e("123", "error: $it")
                }
            )
        compositeDisposable.add(disposable)
    }

    fun getImage(name: String, price: String): Bitmap {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getSavedImage(fileName)
    }

    @SuppressLint("CheckResult")
    fun onSearchItemClicked(itemData: ItemData) {
        val list: ArrayList<SoldData> = marketSoldItems.value as ArrayList<SoldData>
        var isExist = false
        for (i in list.indices) {
            val soldData = list[i]
            if (soldData.name == itemData.name && soldData.price == itemData.price) {
                isExist = true
                soldData.count++
                repository.updateSoldItem(soldData).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(
                        {
                            list[i] = soldData
                            marketSoldItems.value = list
                        }, { t ->
                            Log.e(TAG, "on error: $t")
                        }
                    )
                break
            }
        }

        if (isExist) return


        val name = itemData.name
        val price = itemData.price
        val marketData = this.marketData.value!!
        val soldData = SoldData.create(name, price, marketData)
        repository.insertSoldItem(soldData).doOnComplete {

        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                list.add(soldData)
                marketSoldItems.value = list
            }, { t ->
                Log.e(TAG, "on error: $t")
            }
        )
    }

    fun getCopyData(): String {
        val stringBuilder = StringBuilder()
        val marketData = this.marketData.value ?: return ""
        val totalPrice = this.totalPrice
        val soldList: ArrayList<SoldData> = marketSoldItems.value ?: arrayListOf()
        val marketName = marketData.name
        val marketDate = marketData.date
        val marketPrice = marketData.price
        stringBuilder.append(marketName).append(" ").append(marketDate).append("\n總收入: ")
            .append(totalPrice).append("\n\n")

        stringBuilder.append("攤位費 ")
            .append(marketPrice).append("\n\n")

        soldList.forEach { soldItem ->
            stringBuilder.append(soldItem.name).append(" * ").append(soldItem.count).append(" ")
                .append((soldItem.count * soldItem.price.toInt())).append("\n")
        }

        return stringBuilder.toString()
    }

}