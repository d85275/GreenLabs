package chao.greenlabs.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.AddMarketSetDateViewModel
import chao.greenlabs.viewmodels.AddMarketViewModel

class AddMarketSetDateVMFactory(
    private val marketName: String,
    private val marketLocation: String,
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddMarketSetDateViewModel(marketName, marketLocation, repository) as T
    }
}