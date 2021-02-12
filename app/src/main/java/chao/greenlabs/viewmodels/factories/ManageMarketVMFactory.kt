package chao.greenlabs.viewmodels.factories

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.ManageMarketViewModel

class ManageMarketVMFactory(
    private val res: Resources,
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ManageMarketViewModel(res, repository) as T
    }
}