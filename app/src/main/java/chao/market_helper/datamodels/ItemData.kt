package chao.market_helper.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemData(
    @PrimaryKey val name: String,
    var price: String,
    var optionCategory: ArrayList<OptionCategory> = arrayListOf()
)