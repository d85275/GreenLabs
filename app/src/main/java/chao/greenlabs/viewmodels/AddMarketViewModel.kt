package chao.greenlabs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddMarketViewModel(private val repository: Repository) : ViewModel() {

    var marketName: String = ""
    var marketLocation: String = ""

    private var marketList = MutableLiveData<List<MarketData>>(arrayListOf())

    private var newMarketData = MutableLiveData<MarketData>()

    private var addDone = MutableLiveData(false)

    fun getAddDone(): LiveData<Boolean> = addDone

    fun getMarketList(): LiveData<List<MarketData>> = marketList

    fun getNewMarketData(): LiveData<MarketData> = newMarketData

    fun addMarket(date: String, startTime: String, endTime: String, fee: String) {
        val curData: List<MarketData> = marketList.value!!
        val marketData: MarketData
        val list = arrayListOf<MarketData>()
        if (curData.any { it.date == date }) {
            marketData = curData.first { it.date == date }
            marketData.startTime = startTime
            marketData.endTime = endTime
            marketData.fee = fee
            marketData.income = "-$fee"
            list.add(marketData)
            list.addAll(curData.filterNot { it.date == date })
        } else {
            marketData =
                MarketData.create(marketName, fee, marketLocation, date, startTime, endTime)
            list.add(marketData)
            list.addAll(curData)
            newMarketData.value = marketData
        }
        marketList.value = list.sortedBy { it.date }
    }

    fun deleteMarket(position: Int) {
        val curData = marketList.value ?: arrayListOf()
        val list = arrayListOf<MarketData>()
        list.addAll(curData)
        list.removeAt(position)
        marketList.value = list
    }

    fun getMarketData(position: Int): MarketData {
        val curData = marketList.value ?: arrayListOf()
        return curData[position]
    }

    fun getMarketListSize(): Int {
        return marketList.value?.size ?: 0
    }

    fun addMarketList() {
        viewModelScope.launch(Dispatchers.IO) {
            val marketList = marketList.value ?: arrayListOf()
            repository.addMarketList(marketList)
            addDone.postValue(true)
        }
    }

    fun getMarketFee(date: String): String {
        val marketList = marketList.value ?: return ""
        return marketList.find { it.date == date }?.fee ?: ""
    }

    fun clear() {
        marketName = ""
        marketLocation = ""
        marketList = MutableLiveData(arrayListOf())
        newMarketData = MutableLiveData()
        addDone = MutableLiveData(false)
    }
}