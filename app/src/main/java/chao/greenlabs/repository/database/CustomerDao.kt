package chao.greenlabs.repository.database

import androidx.room.*
import chao.greenlabs.datamodels.CustomerData
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customerdata")
    fun getAll(): Single<List<CustomerData>>

    @Query("SELECT * FROM customerdata WHERE customerId IN (:customerId)")
    fun getCustomer(customerId: String): Single<CustomerData>

    @Query("SELECT * FROM customerdata WHERE marketId IN (:marketId)")
    fun getCustomer(marketId: Int): Single<List<CustomerData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(customerData: CustomerData): Completable

    @Update
    fun update(customerData: CustomerData): Completable

    @Delete
    fun delete(customerData: CustomerData): Completable
}