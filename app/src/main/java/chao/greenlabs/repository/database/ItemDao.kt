package chao.greenlabs.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import chao.greenlabs.datamodels.ItemData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ItemDao {
    @Query("SELECT * FROM itemdata")
    fun getAll(): Single<List<ItemData>>

    @Insert
    fun insert(item: ItemData): Completable

    @Delete
    fun delete(item: ItemData): Completable
}