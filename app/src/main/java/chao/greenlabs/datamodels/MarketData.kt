package chao.greenlabs.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarketData(
    @PrimaryKey val id: String,
    val name: String,
    val fee: String,
    var income: String,
    val location: String,
    val date: String
) {

    companion object {
        fun create(name: String, fee: String, location: String, date: String): MarketData {
            return MarketData("$name-$date", name, fee, "-$fee", location, date)
        }
    }
}