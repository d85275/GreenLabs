package chao.greenlabs.repository.database

import androidx.room.TypeConverter
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.datamodels.SoldData
import com.google.gson.Gson
import org.json.JSONObject

class CustomerDataConverter {

    /*

     @PrimaryKey val customerId: String?,
    var soldDataList: ArrayList<SoldItem>?,
    var memo: String,
    var total: Int = 0,
    var marketId: Int
     */
    @TypeConverter
    fun fromCustomerData(customerData: CustomerData): String {
        return JSONObject().apply {
            put("customerId", customerData.customerId)
            put("soldDataList", Gson().toJson(customerData.soldDataList))
            put("memo", customerData.memo)
            put("total", customerData.total)
            put("marketId", customerData.marketId)
        }.toString()
    }

    @TypeConverter
    fun toCustomerData(data: String): CustomerData {
        val json = JSONObject(data)
        val customerId = json.get("customerId").toString()
        val soldDataList = Gson().fromJson<Array<CustomerData.SoldItem>>(
            json.get("soldDataList").toString(),
            Array<CustomerData.SoldItem>::class.java
        ).toList()
        val memo = json.get("memo").toString()
        val total = json.get("total").toString()
        val marketId = json.get("marketId").toString().toInt()
        return CustomerData(
            customerId,
            soldDataList as ArrayList<CustomerData.SoldItem>,
            memo,
            total.toInt(),
            marketId
        )
    }
}