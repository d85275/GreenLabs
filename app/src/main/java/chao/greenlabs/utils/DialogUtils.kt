package chao.greenlabs.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import chao.greenlabs.R
import chao.greenlabs.views.IntNumWatcher


object DialogUtils {

    fun showWrongFormat(context: Context) {
        val msg = context.getString(R.string.wrong_format)
        showDialog(context, null, msg, null, null, null, null, false)
    }

    fun showDelete(context: Context, confirmAction: (() -> Unit)) {
        val msg = context.getString(R.string.delete_msg)
        showQuestion(context, msg, confirmAction)
    }

    fun showDelete(context: Context, confirmAction: (() -> Unit), cancelAction: (() -> Unit)?) {
        val msg = context.getString(R.string.delete_msg)
        showQuestion(context, msg, confirmAction, cancelAction)
    }

    private fun showQuestion(
        context: Context,
        msg: String,
        confirmAction: (() -> Unit),
        cancelAction: (() -> Unit)?
    ) {
        showDialog(context, null, msg, null, null, confirmAction, cancelAction, true)
    }

    fun showQuestion(context: Context, msg: String, confirmAction: (() -> Unit)) {
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

    fun showDatePicker(context: Context, date: String, onDateSetAction: (date: String) -> Unit) {
        val datePickerDialog = DatePickerDialog(context)
        val year = date.split("-")[0].toInt()
        val month = date.split("-")[1].toInt() - 1
        val day = date.split("-")[2].toInt()
        datePickerDialog.updateDate(year, month, day)

        datePickerDialog.setOnDateSetListener { _, y, m, d ->
            val setMonth = if (m + 1 < 10) "0${m + 1}" else (m + 1).toString()
            val setDay = if (d < 10) "0$d" else d.toString()
            onDateSetAction.invoke("$y-$setMonth-$setDay")
        }

        datePickerDialog.show()
    }

    fun showTimePicker(
        context: Context,
        hour: String,
        min: String,
        onTimeSetAction: ((hour: String, min: String) -> Unit)
    ) {
        val timePickerDialog = TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val h = if (hourOfDay > 10) hourOfDay.toString() else "0$hourOfDay"
                val m = if (minute > 10) minute.toString() else "0$minute"
                onTimeSetAction.invoke(h, m)
            },
            hour.toInt(),
            min.toInt(),
            true
        )

        timePickerDialog.show()
    }

    fun showEditText(context: Context, confirmAction: (text: String) -> Unit, text: String) {
        showEdit(context, confirmAction, text, false)
    }

    fun showEditNumber(context: Context, confirmAction: (text: String) -> Unit, text: String) {
        showEdit(context, confirmAction, text, true)
    }

    private fun showEdit(
        context: Context,
        confirmAction: (text: String) -> Unit,
        text: String,
        isNumber: Boolean
    ) {
        val dialog = AlertDialog.Builder(context).create()
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_edit_text, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setView(view)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        val confirmBtn = view.findViewById<TextView>(R.id.tv_confirm)
        val etData = view.findViewById<EditText>(R.id.et_data)
        confirmBtn.setOnClickListener {
            confirmAction.invoke(etData.text.toString())
            dialog.dismiss()
        }

        if (isNumber) {
            etData.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            etData.addTextChangedListener(IntNumWatcher(etData))
        }

        etData.setText(text)
        etData.requestFocus()
        dialog.show()
    }
}