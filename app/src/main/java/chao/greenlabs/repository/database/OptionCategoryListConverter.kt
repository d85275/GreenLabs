package chao.greenlabs.repository.database

import android.util.Log
import androidx.room.TypeConverter
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.datamodels.Option
import chao.greenlabs.datamodels.OptionCategory
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.StringBuilder

private const val strSeparator = "@_@_@"

class OptionCategoryListConverter {
    @TypeConverter
    fun fromCategoryList(categoryList: List<OptionCategory>): String {
        val categoryString = StringBuilder()
        for (i in categoryList.indices) {
            val category = categoryList[i]
            categoryString.append(fromCategory(category))
            if (i != categoryList.lastIndex) {
                categoryString.append(strSeparator)
            }
        }
        return categoryString.toString()
    }

    private fun fromCategory(optionCategory: OptionCategory): String {
        val listData = JSONObject().apply {
            put("optionList", Gson().toJson(optionCategory.optionList))
        }
        listData.put("title", optionCategory.title)
        return listData.toString()
    }

    @TypeConverter
    fun toCategoryList(data: String): ArrayList<OptionCategory> {
        val dataList = data.split(strSeparator)
        val categoryList = arrayListOf<OptionCategory>()

        dataList.forEach {
            categoryList.add(toCategory(it))
        }
        return categoryList
    }

    private fun toCategory(data: String): OptionCategory {
        if (data.isEmpty()) return OptionCategory("", arrayListOf())
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