package chao.greenlabs.repository.database

import androidx.room.*
import chao.greenlabs.datamodels.ItemData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ItemDao {
    @Query("SELECT * FROM itemdata")
    fun getAll(): Single<List<ItemData>>

    @Insert
    fun insert(item: ItemData): Completable

    @Update
    fun update(item: ItemData): Completable

    @Delete
    fun delete(item: ItemData): Completable
}