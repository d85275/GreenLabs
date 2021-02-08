package chao.greenlabs.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MarketData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var name: String,
    var fee: String,
    var income: String,
    var location: String,
    var date: String,
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
            return MarketData(0, name, fee, "-$fee", location, date, startTime, endTime)
        }
    }
}