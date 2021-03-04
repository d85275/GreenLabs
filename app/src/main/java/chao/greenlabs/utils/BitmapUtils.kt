package chao.greenlabs.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
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

        viewModel.bitmapUpdated(true)
    }

    fun loadBitmap(context: Context, bitmap: Bitmap?, imageView: ImageView) {
        Glide.with(context).load(bitmap).into(imageView)
    }

    fun loadDefault(context: Context, imageView: ImageView) {
        Glide.with(context).applyDefaultRequestOptions(
            RequestOptions().error(R.drawable.default_item).placeholder(R.drawable.default_item)
        ).load(R.drawable.default_item).into(imageView)
    }

    fun getBitmapFromImageView(imageView: ImageView): Bitmap {
        val result = Bitmap.createBitmap(
            imageView.width,
            imageView.height,
            Bitmap.Config.RGB_565
        )
        val c = Canvas(result)
        imageView.draw(c)

        return result
    }
}