package chao.market_helper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chao.market_helper.datamodels.MarketData
import chao.market_helper.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ManageMarketViewModel"

class MarketListViewModel(private val repository: Repository) : ViewModel() {

    private val marketList = MutableLiveData<List<MarketData>>()
    private val totalIncome = MutableLiveData<Int>()

    fun getTotalIncome(): LiveData<Int> = totalIncome

    fun getMarketList(): LiveData<List<MarketData>> = marketList

    fun loadMarketData() {
        viewModelScope.launch(Dispatchers.IO) {
            var total = 0
            val list = repository.getMarkets()
            list.forEach { marketData ->
                total += marketData.income.toInt()
            }

            marketList.postValue(list)
            totalIncome.postValue(total)
        }
    }

    fun deleteMarket(position: Int) {
        val marketList = marketList.value ?: return
        if (position >= marketList.size) return

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCustomer(marketList[position].id)
            deleteMarket(marketList[position])
        }
    }

    private fun deleteMarket(marketData: MarketData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMarket(marketData)
            loadMarketData()
        }
    }

}