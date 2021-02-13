package chao.greenlabs.viewmodels

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val TAG = "AddCustomerViewModel"

class AddCustomerViewModel(private val repository: Repository) : ViewModel() {

    private var customerData = MutableLiveData<CustomerData>()

    private val itemList = arrayListOf<ItemData>()
    private var matchedItems = MutableLiveData(arrayListOf<ItemData>())
    private val isCustomerSaved = MutableLiveData(false)

    private var compositeDisposable = CompositeDisposable()

    var isUpdateMode = false

    fun clearData() {
        customerData = MutableLiveData()
        itemList.clear()
        matchedItems = MutableLiveData()
        compositeDisposable.clear()
        compositeDisposable = CompositeDisposable()
        isCustomerSaved.value = false
        isUpdateMode = false
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
        compositeDisposable.add(
            repository.getItems().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e(TAG, "e: $e") }.subscribe { list ->
                    itemList.clear()
                    itemList.addAll(list.reversed())
                }
        )
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

    fun getImage(name: String, price: String): Bitmap {
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

        val disposable =
            if (!isUpdateMode) {
                repository.addCustomer(customerData).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        isCustomerSaved.value = true
                    }, {
                        Log.e("123", "error when adding a new customer. $it")
                    })
            } else {
                repository.updateCustomer(customerData).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        isCustomerSaved.value = true
                    }, {
                        Log.e("123", "error when updating a new customer. $it")
                    })
            }
        compositeDisposable.add(disposable)
    }
}