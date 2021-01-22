package chao.greenlabs.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val TAG = "ManageMarketViewModel"

class MarketListViewModel(private val repository: Repository) : ViewModel() {

    private val marketList = MutableLiveData<List<MarketData>>()
    private val compositeDisposable = CompositeDisposable()
    private val totalIncome = MutableLiveData<Int>()

    fun getTotalIncome(): LiveData<Int> = totalIncome

    fun getMarketList(): LiveData<List<MarketData>> = marketList

    fun loadMarketData() {
        var totalIncome = 0
        compositeDisposable.add(
            repository.getMarkets().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe({ list ->
                marketList.value = list
                list.forEach { marketData ->
                    totalIncome += marketData.income.toInt()
                }
                this.totalIncome.value = totalIncome
                //getAllSoldPrice(totalMarketFee)
            }, { t ->
                Log.e(TAG, "Error when loading saved markets: $t")
            })
        )
    }

    fun clearMarketData() {
        compositeDisposable.clear()
    }
}