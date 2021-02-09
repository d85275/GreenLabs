package chao.greenlabs.datamodels

import android.util.Log

data class CustomerData(
    val customerId: String?,
    var soldDataList: ArrayList<SoldData>?,
    var memo: String,
    var total: Int = 0
) {
    companion object {
        fun createEmptyData(): CustomerData {
            return CustomerData(null, null, "")
        }
    }

    init {
        Log.e("123", "CustomerData, init")
    }
}