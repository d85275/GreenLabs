package chao.greenlabs.datamodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarketData(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
    @ColumnInfo val price: String,
    @ColumnInfo val date: String
) {

    companion object {
        fun create(name: String, price: String, date: String): MarketData {
            return MarketData("$name-$date", name, price, date)
        }
    }
}