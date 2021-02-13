package chao.greenlabs.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import chao.greenlabs.R
import chao.greenlabs.viewmodels.AddItemViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_sold_items.view.*

private const val MAX_SIZE = 1000

object BitmapUtils {

    fun loadBitmap(result: ActivityResult, context: Context, viewModel: AddItemViewModel, imageView: ImageView) {
        val selectedImage: Uri? = result.data?.data
        if (selectedImage == null){
            // camera
            Glide.with(context).load(viewModel.getTmpPath()).skipMemoryCache(true).diskCacheStrategy(
                DiskCacheStrategy.NONE).into(imageView)
        } else{
            // gallery
            Glide.with(context).load(selectedImage).into(imageView)
        }
    }

    fun loadIntoView(context: Context, bitmap:Bitmap?, imageView: ImageView){
        Glide.with(context).applyDefaultRequestOptions(
            RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
        ).load(bitmap).into(imageView)
    }

    private fun getResizedBitmap(image: Bitmap): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = MAX_SIZE
            height = (width / bitmapRatio).toInt()
        } else {
            height = MAX_SIZE
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
}