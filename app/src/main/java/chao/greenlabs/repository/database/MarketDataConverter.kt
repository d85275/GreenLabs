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
            put("location", marketData.location)
            put("fee", marketData.fee)
            put("income", marketData.income)
        }.toString()
    }

    @TypeConverter
    fun toMarketData(data: String): MarketData {
        val json = JSONObject(data)
        val id = json.get("id").toString()
        val name = json.get("name").toString()
        val fee = json.get("fee").toString()
        val income = json.get("income").toString()
        val date = json.get("date").toString()
        val location = json.get("location").toString()
        return MarketData(id, name, fee, income, location, date)
    }
}