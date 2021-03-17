package chao.market_helper.viewmodels

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chao.market_helper.R
import chao.market_helper.datamodels.CustomerData
import chao.market_helper.datamodels.MarketData
import chao.market_helper.repository.Repository
import chao.market_helper.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageMarketViewModel(
    private val res: Resources,
    private val repository: Repository
) : ViewModel() {

    private var marketData = MutableLiveData<MarketData>()

    private var customerList = MutableLiveData<List<CustomerData>>()

    fun getCustomerList(): LiveData<List<CustomerData>> = customerList

    fun getMarketData(): LiveData<MarketData> = marketData

    fun setMarketData(data: MarketData) {
        marketData.value = data
    }

    private var curCustomerSize = -1
    fun addBtnClicked() {
        curCustomerSize = customerList.value?.size ?: 0
    }

    fun loadCustomers() {
        val marketData = this.marketData.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getCustomer(marketData.id).reversed()
            customerList.postValue(list)
            updateMarketIncome(list)
        }
    }

    fun deleteCustomer(customerData: CustomerData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCustomer(customerData)
            loadCustomers()
        }
    }

    fun clearMarketSoldData() {
        marketData = MutableLiveData()
        customerList = MutableLiveData()
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


    private fun updateMarket(data: MarketData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMarket(data)
            marketData.postValue(data)
        }
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
        stringBuilder.append(marketName).append(" ").append(marketDate)
            .append("\n${res.getString(R.string.total_sold_price)}: ")
            .append(totalPrice).append("\n\n")

        stringBuilder.append("${res.getString(R.string.market_price)} ")
            .append(marketPrice).append("\n\n")

        for (i in customerList.indices) {
            val customer = customerList[i]
            if (customer.soldDataList == null) continue
            stringBuilder.append("${res.getString(R.string.customer_divider)}\n")
            stringBuilder.append(res.getString(R.string.customer_no, i + 1)).append("\n\n")
            customer.soldDataList?.forEach {

                // get options
                val options = StringBuilder()
                it.optionCategory.forEach { category ->
                    if (category.title.isNotEmpty()) {
                        category.optionList.forEach { option ->
                            if (option.isSelected) options.append(option.title).append(" ")
                        }
                    }
                }

                stringBuilder.append(it.name)

                val key = StringBuilder().append(it.name)
                if (options.toString().isNotEmpty()) {
                    key.append(" (").append(options.toString().trim()).append(")")
                    stringBuilder.append(" (").append(options.toString().trim()).append(")")
                }

                stringBuilder.append(" * ").append(it.count).append(" ")
                    .append((it.count * it.price.toInt())).append("\n")


                if (details[key.toString()] == null) {
                    details[key.toString()] = it.count
                } else {
                    details[key.toString()] = details[key.toString()]!! + it.count
                }
            }
            if (customer.memo.isNotEmpty()) {
                stringBuilder.append("\n").append("${res.getString(R.string.memo)} ")
                    .append(customer.memo).append("\n")
            }

            if (customer.discount > 0) {
                stringBuilder.append("\n").append("${res.getString(R.string.sub_total)} ")
                    .append(res.getString(R.string.price, customer.total.toString())).append("\n")
                stringBuilder.append("${res.getString(R.string.discount)} ")
                    .append(res.getString(R.string.price, customer.discount.toString()))
                    .append("\n")
            }

            stringBuilder.append("${res.getString(R.string.total_price)} ").append(
                res.getString(
                    R.string.price,
                    (customer.total - customer.discount).toString()
                )
            ).append("\n\n")
        }

        if (details.isEmpty()) {
            LogUtils.debug("\n${stringBuilder.toString()}")
            return stringBuilder.toString()
        }

        stringBuilder.append("${res.getString(R.string.detail_divider_head)}\n")
        details.forEach { (name, count) ->
            stringBuilder.append(name).append(" * ").append(count).append("\n")
        }
        stringBuilder.append("${res.getString(R.string.detail_divider_end)}\n")

        LogUtils.debug("\n${stringBuilder.toString()}")

        return stringBuilder.toString()
    }
}