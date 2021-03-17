package chao.market_helper.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import chao.market_helper.datamodels.CustomerData
import chao.market_helper.datamodels.ItemData
import chao.market_helper.datamodels.MarketData

@Database(
    entities = [ItemData::class, MarketData::class, CustomerData::class],
    version = 1
)
@TypeConverters(
    MarketDataConverter::class,
    SoldListConverter::class,
    OptionCategoryListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun marketDao(): MarketDao
    abstract fun customerDao(): CustomerDao
}