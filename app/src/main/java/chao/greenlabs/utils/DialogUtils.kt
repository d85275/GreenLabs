package chao.greenlabs.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.Window
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import chao.greenlabs.R
import chao.greenlabs.views.MainFragment


object DialogUtils {

    fun showDelete(context: Context, confirmAction: (() -> Unit)) {
        val msg = context.getString(R.string.delete_msg)
        showDialog(context, null, msg, null, null, confirmAction, null, true)
    }

    fun showPickImage(context: Context, cameraAction: (() -> Unit), galleryAction: (() -> Unit)) {
        val msg = context.getString(R.string.image_source)
        val confirmString = context.getString(R.string.camera)
        val cancelString = context.getString(R.string.gallery)

        showDialog(
            context,
            null,
            msg,
            confirmString,
            cancelString,
            cameraAction,
            galleryAction,
            true
        )
    }

    private fun showDialog(
        context: Context,
        title: String?,
        msg: String,
        confirmString: String?,
        cancelString: String?,
        confirmAction: (() -> Unit)?,
        cancelAction: (() -> Unit)?,
        isShowCancel: Boolean
    ) {
        val confirm: String = confirmString ?: context.getString(R.string.confirm)
        val cancel: String = cancelString ?: context.getString(R.string.cancel)
        val dialog = AlertDialog.Builder(context).create()
        if (title.isNullOrEmpty()) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        } else {
            dialog.setTitle(title)
            dialog.setIcon(android.R.drawable.ic_dialog_alert)
        }
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setMessage(msg)

        dialog.setButton(
            AlertDialog.BUTTON_POSITIVE, confirm
        ) { _, _ ->
            confirmAction?.invoke()
            dialog.dismiss()
        }

        if (isShowCancel) {
            dialog.setButton(
                AlertDialog.BUTTON_NEGATIVE, cancel
            ) { _, _ ->
                cancelAction?.invoke()
                dialog.dismiss()
            }
        }

        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isAllCaps = false
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isAllCaps = false
    }
}