package chao.greenlabs.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.CustomerData
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
    private var marketSoldItems = MutableLiveData<ArrayList<SoldData>>()
    private var totalIncome = 0

    private val customerList = MutableLiveData<ArrayList<CustomerData>>()

    fun getCustomerList(): LiveData<ArrayList<CustomerData>> = customerList

    fun getMarketData(): LiveData<MarketData> = marketData

    fun setMarketData(data: MarketData) {
        marketData.value = data
    }

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
            repository.getSoldItems(marketData.id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e(TAG, "e: $e") }.subscribe { list ->
                    getCustomerList(list)
                }
        )
    }

    private fun getCustomerList(soldList: List<SoldData>) {
        val list = arrayListOf<CustomerData>()
        var curCustomer = ""
        soldList.forEach { soldData ->
            val customerId = soldData.customerId
            if (curCustomer != customerId) {
                val items = arrayListOf<SoldData>()
                items.addAll(soldList.filter { it.customerId == customerId })
                val customer = CustomerData(customerId, items, "")
                list.add(customer)
                curCustomer = customerId
            }
        }

        list.add(CustomerData.createEmptyData())
        customerList.value = list
    }

    fun clearMarketSoldData() {
        marketSoldItems = MutableLiveData()
        //compositeDisposable.dispose()
    }

    fun updateMarketName(name: String) {
        val marketData = marketData.value ?: return
        if (name.isEmpty()) return

        marketData.name = name

        updateMarket(marketData)
    }

    fun updateMarketLocation(location: String) {
        val marketData = marketData.value ?: return
        if (location.isEmpty()) return

        marketData.location = location

        updateMarket(marketData)
    }

    fun updateMarketFee(fee: String) {
        val marketData = marketData.value ?: return
        if (fee.isEmpty()) return

        val diff = marketData.fee.toInt() - fee.toInt()
        marketData.fee = fee
        marketData.income = (marketData.income.toInt() + diff).toString()
        updateMarket(marketData)
    }

    fun updateMarketDate(date: String) {
        val marketData = marketData.value ?: return
        if (date.isEmpty()) return

        marketData.date = date
        updateMarket(marketData)
    }

    fun updateMarketStartTime(hour: String, min: String) {
        val marketData = marketData.value ?: return

        marketData.startTime = "$hour : $min"
        updateMarket(marketData)
    }

    fun updateMarketEndTime(hour: String, min: String) {
        val marketData = marketData.value ?: return

        marketData.endTime = "$hour : $min"
        updateMarket(marketData)
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