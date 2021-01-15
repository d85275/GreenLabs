package chao.greenlabs.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.MarketData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MarketDao {
    @Query("SELECT * FROM marketdata ORDER BY id DESC")
    fun getAll(): Single<List<MarketData>>

    @Insert
    fun insert(market: MarketData): Completable

    @Delete
    fun delete(market: MarketData): Completable
}