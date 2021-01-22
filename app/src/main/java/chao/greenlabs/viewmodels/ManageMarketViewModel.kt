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

    private var marketData = MutableLiveData<MarketData>()

    private val itemList = arrayListOf<ItemData>()
    private val matchedItems = MutableLiveData(arrayListOf<ItemData>())
    private var marketSoldItems = MutableLiveData<ArrayList<SoldData>>()
    private val isMarketDeleted = MutableLiveData(false)

    private var totalIncome = 0

    fun getIsMarketDeleted(): LiveData<Boolean> = isMarketDeleted

    fun setIsMarketDeleted(state: Boolean) {
        isMarketDeleted.value = state
    }

    fun getMarketData(): LiveData<MarketData> = marketData

    fun setMarketData(data: MarketData) {
        marketData.value = data
    }

    fun getMatchedItems(): LiveData<ArrayList<ItemData>> = matchedItems

    fun getMarketSoldItems(): LiveData<ArrayList<SoldData>> = marketSoldItems

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

    fun loadMarketSoldData() {
        val marketData = this.marketData.value ?: return
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.getSoldItems(marketData.name, marketData.date).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e(TAG, "e: $e") }.subscribe { list ->
                    marketSoldItems.postValue(list as ArrayList<SoldData>?)
                }
        )
    }

    fun clearMarketSoldData() {
        marketSoldItems = MutableLiveData()
        //compositeDisposable.dispose()
    }

    fun updateMarketIncome(list: List<SoldData>) {
        val marketData = marketData.value ?: return
        var total = 0 - marketData.fee.toInt()
        list.forEach { soldItem ->
            total += (soldItem.price.toInt() * soldItem.count)
        }
        totalIncome = total
        marketData.income = totalIncome.toString()

        val disposable = repository.updateMarket(marketData).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                this.marketData.value = marketData
            }, {
                Log.e(TAG, "error: $it")
            })
        compositeDisposable.add(disposable)
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

    fun deleteMarket() {
        val marketData = marketData.value ?: return
        val disposable = repository.deleteMarket(marketData).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                isMarketDeleted.value = true
            }, {
                Log.e(TAG, "error: $it")
            })
        compositeDisposable.add(disposable)
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
                    Log.e(TAG, "error: $it")
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
        val totalPrice = marketData.income
        val soldList: ArrayList<SoldData> = marketSoldItems.value ?: arrayListOf()
        val marketName = marketData.name
        val marketDate = marketData.date
        val marketPrice = marketData.fee
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