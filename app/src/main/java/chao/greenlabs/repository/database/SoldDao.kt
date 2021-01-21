package chao.greenlabs.repository.database

import androidx.room.*
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.datamodels.SoldData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface SoldDao {
    @Query("SELECT * FROM solddata WHERE marketData IN (:marketData)")
    fun getItems(marketData: MarketData): Single<List<SoldData>>

    @Query("SELECT * FROM solddata")
    fun getItems(): Single<List<SoldData>>

    @Query("SELECT * FROM solddata WHERE name IN (:itemName)")
    fun getItemsByItemName(itemName: String): Single<List<SoldData>>

    @Query("UPDATE solddata SET price=:newPrice, name=:newName WHERE name=:oldName")
    fun updateByItemName(oldName: String, newName: String, newPrice: String): Completable

    @Update
    fun update(soldData: SoldData): Completable

    @Insert
    fun insert(soldData: SoldData): Completable

    @Delete
    fun delete(soldData: SoldData): Completable
}