package chao.greenlabs.datamodels

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import chao.greenlabs.utils.DateTimeUtils

@Entity
data class CustomerData(
    @PrimaryKey val customerId: String,
    var soldDataList: ArrayList<SoldItem>?,
    var memo: String,
    var total: Int = 0,
    var marketId: Int

) {
    companion object {
        fun createEmptyData(): CustomerData {
            return CustomerData("", null, "", 0, -1)
        }

        fun createNewCustomer(marketId: Int): CustomerData {
            return CustomerData(DateTimeUtils.getCustomerId(), arrayListOf(), "", 0, marketId)
        }
    }

    data class SoldItem(val name: String, val price: String, var count: Int)
}