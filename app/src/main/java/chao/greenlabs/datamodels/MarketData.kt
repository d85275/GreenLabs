package chao.greenlabs.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarketData(
    @PrimaryKey val id: String,
    val name: String,
    var fee: String,
    var income: String,
    val location: String,
    val date: String,
    var startTime: String,
    var endTime: String
) {

    companion object {
        fun create(
            name: String,
            fee: String,
            location: String,
            date: String,
            startTime: String,
            endTime: String
        ): MarketData {
            return MarketData("$name-$date", name, fee, "-$fee", location, date, startTime, endTime)
        }
    }
}