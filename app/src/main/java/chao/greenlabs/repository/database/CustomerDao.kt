package chao.greenlabs.repository.database

import androidx.room.*
import chao.greenlabs.datamodels.CustomerData

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customerdata")
    suspend fun getAll(): List<CustomerData>

    @Query("SELECT * FROM customerdata WHERE marketId IN (:marketId)")
    suspend fun getCustomer(marketId: Int): List<CustomerData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customerData: CustomerData)

    @Update
    suspend fun update(customerData: CustomerData)

    @Delete
    suspend fun delete(customerData: CustomerData)

    @Query("DELETE FROM customerdata WHERE marketId IN (:marketId)")
    suspend fun delete(marketId: Int)
}