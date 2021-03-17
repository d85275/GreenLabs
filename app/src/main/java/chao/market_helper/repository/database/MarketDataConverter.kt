package chao.market_helper.repository.database

import androidx.room.TypeConverter
import chao.market_helper.datamodels.MarketData
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
            put("startTime", marketData.startTime)
            put("endTime", marketData.endTime)
        }.toString()
    }

    @TypeConverter
    fun toMarketData(data: String): MarketData {
        val json = JSONObject(data)
        val id = json.get("id").toString().toInt()
        val name = json.get("name").toString()
        val fee = json.get("fee").toString()
        val income = json.get("income").toString()
        val date = json.get("date").toString()
        val location = json.get("location").toString()
        val startTime = json.get("startTime").toString()
        val endTime = json.get("endTime").toString()
        return MarketData(id, name, fee, income, location, date, startTime, endTime)
    }
}