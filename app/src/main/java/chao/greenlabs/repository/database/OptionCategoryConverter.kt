package chao.greenlabs.repository.database

import androidx.room.TypeConverter
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.datamodels.Option
import chao.greenlabs.datamodels.OptionCategory
import com.google.gson.Gson
import org.json.JSONObject

class OptionCategoryConverter {

    @TypeConverter
    fun fromOptionCategory(optionCategory: OptionCategory): String {
        return JSONObject().apply {
            put("title", optionCategory.title)
            put("optionList", put("optionList", Gson().toJson(optionCategory.optionList)))
        }.toString()
    }

    @TypeConverter
    fun toOptionCategory(data: String): OptionCategory {
        val json = JSONObject(data)
        val title = json.get("title").toString()
        val optionList = ArrayList(
            Gson().fromJson<Array<Option>>(
                json.get("optionList").toString(),
                Array<Option>::class.java
            ).toList()
        )
        return OptionCategory(title, optionList)
    }
}