package chao.greenlabs.viewmodels

import android.content.res.Resources
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.R
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val TAG = "ManageMarketViewModel"

class ManageMarketViewModel(
    private val res: Resources,
    private val repository: Repository
) : ViewModel() {

    private var marketData = MutableLiveData<MarketData>()

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
            total -= customerData.discount
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
        val customerList = this.customerList.value ?: arrayListOf()
        val details = hashMapOf<String, Int>()
        val totalPrice = marketData.income
        val marketName = marketData.name
        val marketDate = marketData.date
        val marketPrice = marketData.fee
        stringBuilder.append(marketName).append(" ").append(marketDate).append("\n總收入: ")
            .append(totalPrice).append("\n\n")

        stringBuilder.append("攤位費 ")
            .append(marketPrice).append("\n\n")

        for (i in customerList.indices) {
            val customer = customerList[i]
            if (customer.soldDataList == null) continue
            stringBuilder.append("----------------------\n")
            stringBuilder.append(res.getString(R.string.customer_no, i + 1)).append("\n\n")
            customer.soldDataList?.forEach {
                stringBuilder.append(it.name).append(" * ").append(it.count).append(" ")
                    .append((it.count * it.price.toInt())).append("\n")

                if (details[it.name] == null) {
                    details[it.name] = 1
                } else {
                    details[it.name] = details[it.name]!! + 1
                }
            }
            if (customer.memo.isNotEmpty()) {
                stringBuilder.append("\n").append("備註 ").append(customer.memo).append("\n")
            }
            stringBuilder.append("\n").append("小計 ")
                .append(res.getString(R.string.price, customer.total.toString())).append("\n")
            stringBuilder.append("折扣 ")
                .append(res.getString(R.string.price, customer.discount.toString())).append("\n")
            stringBuilder.append("總金額 ").append(
                res.getString(
                    R.string.price,
                    (customer.total - customer.discount).toString()
                )
            ).append("\n\n")
        }

        stringBuilder.append("========明細========\n")
        details.forEach { (name, count) ->
            stringBuilder.append(name).append(" * ").append(count).append("\n")
        }
        stringBuilder.append("====================\n")

        return stringBuilder.toString()
    }

    fun getImage(name: String, price: String): Bitmap {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getSavedImage(fileName)
    }

}