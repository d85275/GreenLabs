package chao.greenlabs.views.viewholders.options

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.datamodels.OptionCategory

open class BaseViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

    open fun bindView(optionCategory: OptionCategory?, position: Int) {}
}