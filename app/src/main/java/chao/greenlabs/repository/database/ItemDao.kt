package chao.greenlabs.repository.database

import androidx.room.*
import chao.greenlabs.datamodels.ItemData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ItemDao {
    @Query("SELECT * FROM itemdata")
    suspend fun getAll(): List<ItemData>

    @Insert
    suspend fun insert(item: ItemData)

    @Update
    suspend fun update(item: ItemData)

    @Delete
    suspend fun delete(item: ItemData)
}