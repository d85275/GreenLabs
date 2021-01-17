package chao.greenlabs.repository.database

import androidx.room.TypeConverter
import chao.greenlabs.datamodels.MarketData
import org.json.JSONObject

class MarketDataConverter {

    @TypeConverter
    fun fromMarketData(marketData: MarketData): String {
        return JSONObject().apply {
            put("id", marketData.id)
            put("name", marketData.name)
            put("date", marketData.date)
            put("price", marketData.price)
        }.toString()
    }

    @TypeConverter
    fun toMarketData(data: String): MarketData {
        val json = JSONObject(data)
        val id = json.get("id").toString()
        val name = json.get("name").toString()
        val price = json.get("price").toString()
        val date = json.get("date").toString()
        return MarketData(id, name, price, date)
    }
}