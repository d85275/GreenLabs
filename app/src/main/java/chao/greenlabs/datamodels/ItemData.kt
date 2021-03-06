package chao.greenlabs.datamodels

import android.graphics.Bitmap
import android.util.Log
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.AddCustomerViewModel

@Entity
data class ItemData(
    @PrimaryKey val name: String,
    var price: String,
    var optionCategory: ArrayList<OptionCategory> = arrayListOf()
) {
    @Ignore
    private var bitmap: Bitmap? = null
    fun loadImage(repository: Repository) {
        bitmap = repository.getSavedImage(name)
    }

    fun getImage(): Bitmap? = bitmap
}