package chao.greenlabs.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddMarketViewModel(private val repository: Repository) : ViewModel() {

    private val message = MutableLiveData<String>()

    fun getMessage(): LiveData<String> = message

    fun addMarket(name: String, price: String, date: String) {
        if (name.isEmpty() || price.isEmpty() || date.isEmpty()) {
            message.postValue("Please fill in all the fields")
            return
        }
        val marketData = MarketData.create(name, price, date)
        repository.addMarket(marketData).doOnComplete {
            message.postValue("Market is saved")
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
    }
}