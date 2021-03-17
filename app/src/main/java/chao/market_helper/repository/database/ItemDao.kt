package chao.market_helper.repository.database

import androidx.room.*
import chao.market_helper.datamodels.ItemData

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