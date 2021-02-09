package chao.greenlabs.repository.database

import androidx.room.*
import chao.greenlabs.datamodels.CustomerData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customerdata")
    fun getAll(): Single<List<CustomerData>>

    @Insert
    fun insert(customerData: CustomerData): Completable

    @Update
    fun update(customerData: CustomerData): Completable

    @Delete
    fun delete(customerData: CustomerData): Completable
}