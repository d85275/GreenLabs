package chao.market_helper.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chao.market_helper.datamodels.CustomerData
import chao.market_helper.datamodels.ItemData
import chao.market_helper.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "AddCustomerViewModel"

class AddCustomerViewModel(private val repository: Repository) : ViewModel() {

    private var customerData = MutableLiveData<CustomerData>()

    private val itemList = arrayListOf<ItemData>()
    private var matchedItems = MutableLiveData<ArrayList<ItemData>>()
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

    fun loadItemData() {
        viewModelScope.launch(Dispatchers.IO) {
            itemList.clear()
            itemList.addAll(repository.getItems().reversed())
            matchedItems.postValue(itemList)
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

    @SuppressLint("CheckResult")
    fun onSearchItemClicked(itemData: ItemData) {
        val customerData = customerData.value ?: return
        val soldList = customerData.soldDataList ?: return

        val soldData = existSoldData(soldList, itemData)

        if (soldData != null) {
            soldList.find { it.name == itemData.name && it.price == itemData.price }!!.count++
            customerData.total += soldData.price.toInt()
        } else {
            val soldItem =
                CustomerData.SoldItem(itemData.name, itemData.price, 1, itemData.optionCategory)
            soldList.add(soldItem)
            customerData.total += soldItem.price.toInt()
        }

        customerData.soldDataList = soldList
        this.customerData.value = customerData
    }

    private fun existSoldData(
        soldList: List<CustomerData.SoldItem>,
        itemData: ItemData
    ): CustomerData.SoldItem? {
        return soldList.find {
            it.name == itemData.name && it.price == itemData.price &&
                    it.optionCategory.containsAll(itemData.optionCategory) &&
                    itemData.optionCategory.containsAll(it.optionCategory)
        }
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
                isCustomerAdded = true
            } else {
                repository.updateCustomer(customerData)
            }
            isCustomerSaved.postValue(true)
        }
    }

    private var isCustomerAdded = false
    fun getIsCustomerAdded(): Boolean = isCustomerAdded
    fun setIsCustomerAdded(state: Boolean) {
        isCustomerAdded = state
    }
}