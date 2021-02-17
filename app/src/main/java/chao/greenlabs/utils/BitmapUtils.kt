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

object BitmapUtils {

    fun getBitmapFromSource(
        result: ActivityResult,
        context: Context,
        viewModel: AddItemViewModel,
        imageView: ImageView
    ) {
        val selectedImage: Uri? = result.data?.data
        if (selectedImage == null) {
            // camera
            Glide.with(context).load(viewModel.getTmpPath()).skipMemoryCache(true)
                .diskCacheStrategy(
                    DiskCacheStrategy.NONE
                ).into(imageView)
        } else {
            // gallery
            Glide.with(context).load(selectedImage).into(imageView)
        }
    }

    fun loadBitmap(context: Context, bitmap: Bitmap?, imageView: ImageView) {
        Glide.with(context).applyDefaultRequestOptions(
            RequestOptions().error(R.drawable.default_item)
        ).load(bitmap).into(imageView)
    }

    fun loadDefault(context: Context, imageView: ImageView) {
        Glide.with(context).load(R.drawable.default_item).into(imageView)
    }
}