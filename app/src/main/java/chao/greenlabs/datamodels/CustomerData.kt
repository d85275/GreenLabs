package chao.greenlabs.datamodels

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.DateTimeUtils

@Entity
data class CustomerData(
    @PrimaryKey val customerId: String,
    var soldDataList: ArrayList<SoldItem>?,
    var memo: String,
    var discount: Int = 0,
    var total: Int = 0,
    var marketId: Int

) {
    companion object {
        fun createNewCustomer(marketId: Int): CustomerData {
            return CustomerData(DateTimeUtils.getCustomerId(), arrayListOf(), "", 0, 0, marketId)
        }
    }

    data class SoldItem(val name: String, val price: String, var count: Int) {
        @Ignore
        private var bitmap: Bitmap? = null

        private var isImageLoaded = false
        fun getImage(repository: Repository): Bitmap? {
            if (isImageLoaded) return bitmap
            return repository.getSavedImage(name)
        }
    }
}