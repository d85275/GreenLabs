package chao.market_helper.repository

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import androidx.room.Room
import chao.market_helper.datamodels.CustomerData
import chao.market_helper.datamodels.ItemData
import chao.market_helper.datamodels.MarketData
import chao.market_helper.repository.database.AppDatabase
import java.io.File
import java.io.FileOutputStream


class Repository(private val context: Context) {

    // local

    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "database-name").build()

    suspend fun addCustomer(customerData: CustomerData) {
        db.customerDao().insert(customerData)
    }

    suspend fun updateCustomer(customerData: CustomerData) {
        db.customerDao().update(customerData)
    }

    suspend fun getCustomer(marketId: Int): List<CustomerData> {
        return db.customerDao().getCustomer(marketId)
    }

    suspend fun deleteCustomer(marketId: Int) {
        db.customerDao().delete(marketId)
    }

    suspend fun deleteCustomer(customerData: CustomerData) {
        db.customerDao().delete(customerData)
    }

    suspend fun addMarketList(marketList: List<MarketData>) {
        db.marketDao().insertAll(marketList)
    }

    suspend fun deleteMarket(marketData: MarketData) {
        db.marketDao().delete(marketData)
    }

    suspend fun updateMarket(marketData: MarketData) {
        db.marketDao().update(marketData)
    }

    suspend fun getMarkets(): List<MarketData> {
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