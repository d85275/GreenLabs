package chao.greenlabs.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.AddCustomerViewModel
import chao.greenlabs.viewmodels.ManageMarketViewModel

class AddCustomerVMFactory(private val repository: Repository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddCustomerViewModel(repository) as T
    }
}