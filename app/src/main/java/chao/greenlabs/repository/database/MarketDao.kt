package chao.greenlabs.repository.database

import androidx.room.*
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.MarketData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MarketDao {
    @Query("SELECT * FROM marketdata ORDER BY date ASC")
    fun getAll(): Single<List<MarketData>>

    @Insert
    fun insert(market: MarketData): Completable

    @Delete
    fun delete(market: MarketData): Completable

    @Update
    fun update(market: MarketData): Completable
}