package chao.greenlabs.viewmodels

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "AddCustomerViewModel"

class AddCustomerViewModel(private val repository: Repository) : ViewModel() {

    private var customerData = MutableLiveData<CustomerData>()

    private val itemList = arrayListOf<ItemData>()
    private var matchedItems = MutableLiveData(itemList)
    private val isCustomerSaved = MutableLiveData(false)

    var isUpdateMode = false

    fun clearData() {
        customerData = MutableLiveData()
        itemList.clear()
        matchedItems = MutableLiveData()
        isCustomerSaved.value = false
        isUpdateMode = false
    }

    fun resetData() {
        matchedItems = MutableLiveData(itemList)
    }

    fun setIsCustomerSaved(value: Boolean) {
        isCustomerSaved.value = value
    }

    fun getIsCustomerSaved(): LiveData<Boolean> = isCustomerSaved

    fun setCustomer(data: CustomerData) {
        customerData.value = data
    }

    fun getMatchedItems(): LiveData<ArrayList<ItemData>> = matchedItems

    fun getCustomerData(): LiveData<CustomerData> = customerData

    fun getItemList(): List<ItemData> = itemList

    fun loadItemData() {
        viewModelScope.launch(Dispatchers.IO) {
            itemList.clear()
            itemList.addAll(repository.getItems().reversed())
            itemList.forEach {
                it.loadImage(repository)
            }
        }
    }

    fun deleteSoldItem(position: Int) {
        val customerData = customerData.value ?: return
        val list = customerData.soldDataList ?: return
        customerData.total -= list[position].price.toInt()
        list.removeAt(position)

        customerData.soldDataList = list
        this.customerData.value = customerData
    }

    fun updateCount(position: Int, count: Int) {
        val customerData = customerData.value ?: return
        val list = customerData.soldDataList ?: return
        list[position].count += count
        customerData.total += (count * list[position].price.toInt())

        customerData.soldDataList = list
        this.customerData.value = customerData
    }

    fun updateDiscount(discount: String) {
        if (discount.isEmpty()) return
        val customerData = customerData.value ?: return

        customerData.discount = discount.toInt()
        this.customerData.value = customerData
    }

    fun getImage(name: String, price: String): Bitmap? {
        val fileName = StringBuilder().append(name).append("_").append(price).toString()
        return repository.getSavedImage(fileName)
    }

    @SuppressLint("CheckResult")
    fun onSearchItemClicked(itemData: ItemData) {
        val customerData = customerData.value ?: return
        val soldList = customerData.soldDataList ?: return

        val soldData = soldList.find { it.name == itemData.name && it.price == itemData.price }

        if (soldData != null) {
            soldList.find { it.name == itemData.name && it.price == itemData.price }!!.count++
            customerData.total += soldData.price.toInt()
        } else {
            val soldItem = CustomerData.SoldItem(itemData.name, itemData.price, 1)
            soldList.add(soldItem)
            customerData.total += soldItem.price.toInt()
        }

        customerData.soldDataList = soldList
        this.customerData.value = customerData
    }

    fun onSearch(text: String) {
        if (text.isEmpty()) {
            matchedItems.value = itemList
            return
        }

        val list =
            itemList.filter { itemData ->
                itemData.name.contains(text) || itemData.price.contains(
                    text
                )
            }

        matchedItems.value = list as ArrayList<ItemData>
    }

    fun saveCustomer(memo: String) {
        val customerData = customerData.value ?: return
        customerData.memo = memo.trim()

        viewModelScope.launch(Dispatchers.IO) {
            if (!isUpdateMode) {
                repository.addCustomer(customerData)
            } else {
                repository.updateCustomer(customerData)
            }
            isCustomerSaved.postValue(true)
        }
    }
}