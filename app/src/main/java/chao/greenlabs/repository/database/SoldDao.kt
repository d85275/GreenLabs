package chao.greenlabs.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.datamodels.SoldData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface SoldDao {
    @Query("SELECT * FROM solddata WHERE marketData IN (:marketData)")
    fun getAll(marketData: MarketData): Single<List<SoldData>>

    @Update
    fun update(soldData: SoldData): Completable

    @Insert
    fun insert(soldData: SoldData): Completable
}