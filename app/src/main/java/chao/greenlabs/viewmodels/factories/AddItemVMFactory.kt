package chao.greenlabs.viewmodels.factories

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.AddItemViewModel

class AddItemVMFactory(
    private val resources: Resources,
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddItemViewModel(resources, repository) as T
    }
}