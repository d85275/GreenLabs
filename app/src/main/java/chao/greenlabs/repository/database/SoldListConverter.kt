package chao.greenlabs.repository.database

import androidx.room.TypeConverter
import chao.greenlabs.datamodels.CustomerData
import com.google.gson.Gson
import org.json.JSONObject

class SoldListConverter {
    @TypeConverter
    fun fromSoldList(soldList:List<CustomerData.SoldItem>): String {
        return JSONObject().apply {
            put("soldDataList", Gson().toJson(soldList))
        }.toString()
    }

    @TypeConverter
    fun toSoldList(data: String): ArrayList<CustomerData.SoldItem> {
        val json = JSONObject(data)
        return ArrayList(Gson().fromJson<Array<CustomerData.SoldItem>>(
            json.get("soldDataList").toString(),
            Array<CustomerData.SoldItem>::class.java
        ).toList())
    }
}