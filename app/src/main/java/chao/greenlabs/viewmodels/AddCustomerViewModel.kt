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
import chao.greenlabs.utils.DateTimeUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val TAG = "AddCustomerViewModel"

class AddCustomerViewModel(private val repository: Repository) : ViewModel() {


    private var marketData = MutableLiveData<MarketData>()

    private val itemList = arrayListOf<ItemData>()
    private val matchedItems = MutableLiveData(arrayListOf<ItemData>())
    private var marketSoldItems = MutableLiveData<ArrayList<SoldData>>()
    private var totalIncome = 0

    var compositeDisposable = CompositeDisposable()

    fun getMatchedItems(): LiveData<ArrayList<ItemData>> = matchedItems

    fun getMarketSoldItems(): LiveData<ArrayList<SoldData>> = marketSoldItems

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

    fun updateMarketIncome(list: List<SoldData>) {
        val marketData = marketData.value ?: return
        var total = 0 - marketData.fee.toInt()
        list.forEach { soldItem ->
            total += (soldItem.price.toInt() * soldItem.count)
        }
        totalIncome = total
        marketData.income = totalIncome.toString()

        updateMarket(marketData)

        /*
        val disposable = repository.updateMarket(marketData).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                setMarketData(marketData)
            }, {
                Log.e(TAG, "error: $it")
            })
        compositeDisposable.add(disposable)
         */
    }

    private fun updateMarket(marketData: MarketData) {
        val disposable = repository.updateMarket(marketData).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                this.marketData.value = marketData
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
        val soldData = SoldData.create(name, price, marketData, DateTimeUtils.getCustomerId())
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

    fun onSearch(text: String) {
        val list =
            itemList.filter { itemData ->
                itemData.name.contains(text) || itemData.price.contains(
                    text
                )
            }

        matchedItems.value = list as ArrayList<ItemData>
    }
}