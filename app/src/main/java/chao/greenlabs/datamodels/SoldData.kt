package chao.greenlabs.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SoldData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val price: String,
    var count: Int,
    val marketId: Int,
    val customerId: String
) {
    companion object {
        fun create(
            name: String,
            price: String,
            marketData: MarketData,
            customerId: String
        ): SoldData {
            return SoldData(0, name, price, 1, marketData.id, customerId)
        }
    }
}