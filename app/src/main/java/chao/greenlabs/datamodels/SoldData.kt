package chao.greenlabs.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SoldData(
    @PrimaryKey val id: String,
    val name: String,
    val price: String,
    var count: Int,
    val marketName: String,
    val marketDate: String
) {
    companion object {
        fun create(name: String, price: String, marketData: MarketData): SoldData {
            return SoldData(
                "$name-${marketData.name}-${marketData.date}",
                name,
                price,
                1,
                marketData.name,
                marketData.date
            )
        }
    }

    fun getSoldDataId(): String {
        return "$name-$marketName-$marketDate"
    }
}