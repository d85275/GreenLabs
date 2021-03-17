package chao.market_helper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chao.market_helper.datamodels.ItemData
import chao.market_helper.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemListViewModel(private val repository: Repository) : ViewModel() {

    private val itemList = MutableLiveData<List<ItemData>>()

    fun getItemList(): LiveData<List<ItemData>> = itemList

    fun loadItemData() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getItems().reversed()
            itemList.postValue(list)
        }
    }

    fun deleteItem(position: Int) {
        val itemList = itemList.value ?: return
        val item = itemList[position]
        val name = item.name
        val price = item.price

        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(item)
            loadItemData()
            deleteImage(name, price)
        }
    }

    private fun deleteImage(name: String, price: String) {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        repository.deleteImage(fileName)
    }
}