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

    fun getMarketList(): LiveData<List<MarketData>> = marketList

    fun loadMarketData() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.getMarkets().subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).doOnError { e -> Log.e(TAG, "Error when loading saved markets: $e") }
                .subscribe { list ->
                    marketList.postValue(list)
                }
        )
    }

    fun getAllMarketCost(marketList: List<MarketData>): Int {
        var total = 0
        marketList.forEach { marketData ->
            total -= marketData.price.toInt()
        }
        return total
    }
}