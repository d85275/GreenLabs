package chao.greenlabs.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import androidx.room.Room
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.repository.database.AppDatabase
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream


class Repository(private val context: Context) {

    // local

    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "database-name").build()

    fun addCustomer(customerData: CustomerData): Completable {
        return db.customerDao().insert(customerData)
    }

    fun updateCustomer(customerData: CustomerData): Completable {
        return db.customerDao().update(customerData)
    }

    fun getCustomers(): Single<List<CustomerData>> {
        return db.customerDao().getAll()
    }

    fun getCustomer(customerId: String) {

    }

    fun getCustomer(marketId: Int): Single<List<CustomerData>> {
        return db.customerDao().getCustomer(marketId)
    }

    fun deleteCustomer(marketId: Int): Completable {
        return db.customerDao().delete(marketId)
    }

    fun deleteCustomer(customerData: CustomerData): Completable {
        return db.customerDao().delete(customerData)
    }

    fun addMarket(marketData: MarketData): Completable {
        return db.marketDao().insert(marketData)
    }

    fun addMarketList(marketList: List<MarketData>): Completable {
        return db.marketDao().insertAll(marketList)
    }

    fun deleteMarket(marketData: MarketData): Completable {
        return db.marketDao().delete(marketData)
    }

    fun updateMarket(marketData: MarketData): Completable {
        return db.marketDao().update(marketData)
    }

    fun getMarkets(): Single<List<MarketData>> {
        return db.marketDao().getAll()
    }

    suspend fun addItem(itemData: ItemData) {
        db.itemDao().insert(itemData)
    }

    suspend fun deleteItem(itemData: ItemData) {
        return db.itemDao().delete(itemData)
    }

    suspend fun getItems(): List<ItemData> {
        return db.itemDao().getAll()
    }

    suspend fun updateItem(itemData: ItemData) {
        db.itemDao().update(itemData)
    }

    fun getSavedImage(imgName: String): Bitmap? {
        val imageFile = getImagePath(imgName) ?: return null
        return BitmapFactory.decodeFile(imageFile.absolutePath)
    }

    fun deleteImage(imgName: String) {
        val imageFile = getImagePath(imgName)
        imageFile?.delete()
    }

    fun getTmpPath(): File? {
        val path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
        if (!path.exists()) path.mkdirs()
        return File(path, "tmp.png")
    }

    private fun getImagePath(imgName: String): File? {
        val path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null
        if (!path.exists()) path.mkdirs()
        return File(path, "$imgName.png")
    }

    @Throws(Exception::class)
    fun saveImageToExternal(imgName: String, bm: Bitmap) {
        val imageFile = getImagePath(imgName) ?: throw Exception("cannot get the path")
        val out = FileOutputStream(imageFile)
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, out) // Compress Image
            out.flush()
            out.close()

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(
                context,
                arrayOf(imageFile.absolutePath),
                null
            ) { filePath, uri ->
                Log.i("ExternalStorage", "Scanned $filePath:")
                Log.i("ExternalStorage", "-> uri=$uri")
            }
        } catch (e: Exception) {
            throw e
        }
    }


    // remote

}