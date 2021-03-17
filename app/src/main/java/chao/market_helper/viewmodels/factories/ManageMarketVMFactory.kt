package chao.market_helper.viewmodels.factories

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chao.market_helper.repository.Repository
import chao.market_helper.viewmodels.ManageMarketViewModel

class ManageMarketVMFactory(
    private val res: Resources,
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ManageMarketViewModel(res, repository) as T
    }
}