package chao.greenlabs.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.datamodels.SoldData
import chao.greenlabs.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val TAG = "ManageMarketViewModel"

class ManageMarketViewModel(private val repository: Repository) : ViewModel() {

    private var marketData = MutableLiveData<MarketData>()

    private var marketSoldItems = MutableLiveData<ArrayList<SoldData>>()

    private val customerList = MutableLiveData<List<CustomerData>>()

    fun getCustomerList(): LiveData<List<CustomerData>> = customerList

    fun getMarketData(): LiveData<MarketData> = marketData

    fun setMarketData(data: MarketData) {
        marketData.value = data
    }

    var compositeDisposable = CompositeDisposable()


    fun loadCustomers() {
        val marketData = this.marketData.value ?: return
        val disposable = repository.getCustomer(marketData.id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ list ->
                (list as ArrayList).add(CustomerData.createEmptyData())
                customerList.value = list
                updateMarketIncome(list)
            }, {
                Log.e("123", "error when loading customers: $it")
            })
        compositeDisposable.add(disposable)
    }

    fun clearMarketSoldData() {
        //compositeDisposable.dispose()
    }

    private fun updateMarketIncome(list: List<CustomerData>) {
        val marketData = marketData.value ?: return
        val fee = marketData.fee.toInt()
        var total = 0
        list.forEach { customerData ->
            total += customerData.total
        }
        total -= fee
        marketData.income = total.toString()
        updateMarket(marketData)
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

    fun getImage(name: String, price: String): Bitmap {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getSavedImage(fileName)
    }

}