package chao.greenlabs.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import chao.greenlabs.R
import chao.greenlabs.viewmodels.AddItemViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File

object ImageUtils {

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

    fun loadImage(context: Context, itemName: String, imageView: ImageView) {
        val path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (path?.exists() != true) path?.mkdirs()
        val file = File(path, "$itemName.png")
        Glide.with(context).applyDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.default_item)
        ).load(file).into(imageView)
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