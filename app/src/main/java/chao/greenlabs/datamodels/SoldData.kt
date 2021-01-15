package chao.greenlabs.datamodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SoldData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val price: String,
    @ColumnInfo val marketData: MarketData
) {
    companion object {
        fun create(name: String, price: String, marketData: MarketData): SoldData {
            return SoldData(0, name, price, marketData)
        }
    }
}