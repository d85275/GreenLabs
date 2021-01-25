package chao.greenlabs.viewmodels

import androidx.lifecycle.ViewModel
import chao.greenlabs.repository.Repository

class AddMarketViewModel(private val repository: Repository) : ViewModel() {

    var marketName = ""
    var marketLocation = ""

    fun resetData() {
        marketName = ""
        marketLocation = ""
    }
}