package chao.greenlabs.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.ItemOptionsViewModel

class ItemOptionsVMFactory(private val repository: Repository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ItemOptionsViewModel(repository) as T
    }
}