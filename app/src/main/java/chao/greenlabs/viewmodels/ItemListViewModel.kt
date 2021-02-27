package chao.greenlabs.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ManageItemViewModel"

class ItemListViewModel(private val repository: Repository) : ViewModel() {

    private val itemList = MutableLiveData<List<ItemData>>()

    fun getItemList(): LiveData<List<ItemData>> = itemList

    fun loadItemData() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getItems().reversed()
            //list.forEach { it.loadImage(repository) }
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