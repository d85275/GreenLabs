package chao.market_helper.repository.database

import androidx.room.*
import chao.market_helper.datamodels.MarketData

@Dao
interface MarketDao {
    @Query("SELECT * FROM marketdata ORDER BY date ASC")
    suspend fun getAll(): List<MarketData>

    @Insert
    suspend fun insert(market: MarketData)

    @Insert
    suspend fun insertAll(marketList: List<MarketData>)

    @Delete
    suspend fun delete(market: MarketData)

    @Update
    suspend fun update(market: MarketData)
}