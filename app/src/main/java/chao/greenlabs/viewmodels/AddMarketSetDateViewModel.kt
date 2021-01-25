package chao.greenlabs.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.InputChecker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddMarketSetDateViewModel(
    val marketName: String,
    val marketLocation: String,
    private val repository: Repository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val marketList = MutableLiveData<List<MarketData>>(arrayListOf())

    private val newMarketData = MutableLiveData<MarketData>()

    private val addDone = MutableLiveData(false)

    fun getAddDone(): LiveData<Boolean> = addDone

    fun getMarketList(): LiveData<List<MarketData>> = marketList

    fun getNewMarketData(): LiveData<MarketData> = newMarketData

    fun addMarket(date: String, startTime: String, endTime: String, fee: String) {
        if (InputChecker.validInput(date, startTime, endTime, fee)) {
            val marketData =
                MarketData.create(marketName, fee, marketLocation, date, startTime, endTime)

            val curData: List<MarketData> = marketList.value!!
            val list = arrayListOf(marketData)
            list.addAll(curData)
            newMarketData.value = marketData
            marketList.value = list.sortedBy { it.date }
        }
    }

    fun onDoneClicked() {
        val marketList = marketList.value ?: arrayListOf()
        val disposable = repository.addMarketList(marketList).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                addDone.value = true
            }, {
                Log.e("AddMarketSetDateVM", "error when add market list: $it")
            })
        compositeDisposable.add(disposable)
    }
}