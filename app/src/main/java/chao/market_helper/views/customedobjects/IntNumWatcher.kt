package chao.market_helper.views.customedobjects

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class IntNumWatcher(private val editText: EditText) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        var text = s.toString()
        if (text.contains(".")) {
            text = text.replace(".", "")
            editText.setText(text)
            editText.setSelection(editText.text.length)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}