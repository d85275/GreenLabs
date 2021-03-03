package chao.greenlabs.viewmodels

import android.content.res.Resources
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.*
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.OptionCategory
import chao.greenlabs.datamodels.Option
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.BitmapUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class AddItemViewModel(
    private val res: Resources,
    private val repository: Repository
) : ViewModel() {

    private val msg = MutableLiveData<String>()
    private val updatedItem = MutableLiveData<ItemData>()

    private var optionCategories = MutableLiveData<MutableList<OptionCategory?>>()

    private var isUpdateMode = false

    private var bitmapUpdated: Boolean = false

    fun bitmapUpdated(state: Boolean) {
        bitmapUpdated = state
    }

    fun getOptionCategories(): LiveData<MutableList<OptionCategory?>> = optionCategories

    fun loadOptions() {
        val list = arrayListOf<OptionCategory?>()
        list.add(null)
        optionCategories.value = list
    }

    private fun resetCategory() {
        val list = optionCategories.value ?: arrayListOf()
        list.clear()
        list.add(null)
        optionCategories.postValue(list)
    }

    fun editOptionTitle(
        default: String,
        value: String,
        categoryPosition: Int,
        optionPosition: Int
    ) {
        if (categoryPosition < 0) return
        optionCategories.value ?: return
        val options = optionCategories.value!!
        options[categoryPosition]?.optionList?.get(optionPosition)?.title =
            if (value.trim().isEmpty()) {
                default
            } else {
                value
            }
        optionCategories.value = options
    }

    fun editOptionPrice(
        default: String,
        value: String,
        categoryPosition: Int,
        optionPosition: Int
    ) {
        if (categoryPosition < 0) return
        optionCategories.value ?: return
        val options = optionCategories.value!!
        options[categoryPosition]?.optionList?.get(optionPosition)?.addPrice =
            if (value.trim().isEmpty()) {
                default
            } else {
                value
            }
        optionCategories.value = options
    }

    fun removeOption(categoryPosition: Int, optionPosition: Int) {
        if (categoryPosition < 0) return
        optionCategories.value ?: return
        val options = optionCategories.value!!
        options[categoryPosition]?.optionList?.removeAt(optionPosition)
        optionCategories.value = options
    }

    fun addOption(defaultTitle: String, categoryPosition: Int) {
        if (categoryPosition < 0) return
        optionCategories.value ?: return
        val options = optionCategories.value!!
        options[categoryPosition]?.optionList?.add(Option(defaultTitle, "0"))
        optionCategories.value = options
    }

    fun removeCategory(position: Int) {
        if (position < 0) return
        optionCategories.value ?: return
        val options = optionCategories.value!!
        options.removeAt(position)
        optionCategories.value = options
    }

    fun addCategory(defaultTitle: String) {
        optionCategories.value ?: return
        val options = optionCategories.value!!.filterNotNull()
        val list: ArrayList<OptionCategory?> = arrayListOf()
        list.addAll(options)
        list.add(OptionCategory(defaultTitle, arrayListOf()))
        list.add(null)
        optionCategories.value = list
    }

    fun editCategoryTitle(categoryPosition: Int, default: String, value: String) {
        if (categoryPosition < 0) return
        val options = optionCategories.value ?: return
        options[categoryPosition]?.title =
            if (value.trim().isEmpty()) {
                default
            } else {
                value
            }
        optionCategories.value = options
    }

    fun getMessage(): LiveData<String> = msg

    fun getUpdatedItem(): LiveData<ItemData> = updatedItem

    fun getIsUpdateMode(): Boolean {
        return isUpdateMode
    }

    fun setUpdatedItem(itemData: ItemData) {
        updatedItem.value = itemData
        isUpdateMode = true
    }

    fun clearUpdatedItem() {
        updatedItem.value = null
        isUpdateMode = false
        msg.value = ""
        bitmapUpdated(false)
    }

    private fun saveBitmap(name: String, price: String, imageView: ImageView) {
        if (!bitmapUpdated) return

        val bitmap = BitmapUtils.getBitmapFromImageView(imageView)
        try {
            val fileName = StringBuilder().append(name).append("_").append(price).toString()
            repository.saveImageToExternal(fileName, bitmap)

        } catch (e: Exception) {
            msg.postValue(res.getString(R.string.save_image_failed))
            return
        }
    }

    private fun printOptionData() {
        val optionCategories = this.optionCategories.value
        if (optionCategories == null) {
            Log.e("13", "empty option category")
        }

        optionCategories!!.forEach { category ->
            if (category != null && category.optionList.isNotEmpty()) {
                Log.e("123", "category: ${category.title}")
                category.optionList.forEach { option ->
                    Log.e("123", "option: ${option.title}")
                }
            }
        }
    }

    fun onConfirmClicked(name: String, price: String, imageView: ImageView) {
        viewModelScope.launch(Dispatchers.IO) {
            saveBitmap(name, price, imageView)

            val data = ItemData(name, price)
            val updatedItem = updatedItem.value

            // todo remove this part - for testing only
            printOptionData()

            try {
                when {
                    updatedItem == null -> {
                        repository.addItem(data)
                        msg.postValue(res.getString(R.string.item_saved))
                    }

                    updatedItem.name == data.name -> {
                        repository.updateItem(data)
                        msg.postValue(res.getString(R.string.item_updated))
                    }

                    else -> {
                        repository.deleteItem(updatedItem)
                        val fileName =
                            StringBuilder().append(name).append("_").append(price).toString()
                        repository.deleteImage(fileName)
                        repository.addItem(data)
                        msg.postValue(res.getString(R.string.item_updated))
                    }
                }
            } catch (e: SQLiteConstraintException) {
                msg.postValue(res.getString(R.string.item_exist))
            } catch (e: java.lang.Exception) {
                msg.postValue(res.getString(R.string.add_item_failed))
            } finally {
                resetCategory()
            }
        }

    }

    fun getTmpPath(): File? {
        return repository.getTmpPath()
    }
}