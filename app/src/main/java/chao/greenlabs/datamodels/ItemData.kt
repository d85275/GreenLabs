package chao.greenlabs.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemData(
    @PrimaryKey val name: String,
    val price: String
) {
}