package chao.greenlabs.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.MarketData

@Database(entities = [ItemData::class, MarketData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun marketDao(): MarketDao
}