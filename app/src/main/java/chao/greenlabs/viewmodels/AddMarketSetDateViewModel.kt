package chao.greenlabs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.InputChecker

class AddMarketSetDateViewModel(
    val marketName: String,
    val marketLocation: String,
    private val repository: Repository
) : ViewModel() {
    private val marketList = MutableLiveData<List<MarketData>>(arrayListOf())

    fun getMarketList(): LiveData<List<MarketData>> = marketList

    fun addMarket(date: String, startTime: String, endTime: String, fee: String) {
        if (InputChecker.validInput(date, startTime, endTime, fee)) {
            val marketData =
                MarketData.create(marketName, fee, marketLocation, date, startTime, endTime)

            val curData: List<MarketData> = marketList.value!!
            val list = arrayListOf<MarketData>()
            list.add(marketData)
            list.addAll(curData)
            marketList.value = list.sortedBy { it.date }
        }
    }
}