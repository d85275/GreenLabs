package chao.greenlabs.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.MarketData

@Database(
    entities = [ItemData::class, MarketData::class, CustomerData::class],
    version = 1
)
@TypeConverters(MarketDataConverter::class, SoldListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun marketDao(): MarketDao
    abstract fun customerDao(): CustomerDao
}