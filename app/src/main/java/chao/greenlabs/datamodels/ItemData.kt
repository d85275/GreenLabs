package chao.greenlabs.datamodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val price: String
) {
    companion object {
        fun create(name: String, price: String): ItemData {
            return ItemData(0, name, price)
        }
    }
}