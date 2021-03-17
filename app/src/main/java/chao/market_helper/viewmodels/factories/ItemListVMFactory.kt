package chao.market_helper.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chao.market_helper.repository.Repository
import chao.market_helper.viewmodels.ItemListViewModel

class ItemListVMFactory(private val repository: Repository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ItemListViewModel(repository) as T
    }
}