package chao.greenlabs.datamodels

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomerData(
    @PrimaryKey val customerId: String?,
    var soldDataList: ArrayList<SoldItem>?,
    var memo: String,
    var total: Int = 0,
    var marketId: Int

) {
    companion object {
        fun createEmptyData(): CustomerData {
            return CustomerData(null, null, "", 0, -1)
        }
    }

    init {
        Log.e("123", "CustomerData, init")
    }

    data class SoldItem(val name: String, val price: String, var count: Int)
}