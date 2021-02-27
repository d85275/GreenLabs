package chao.greenlabs.views.viewholders.addoption

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.datamodels.Option

open class BaseViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

    open fun bindView(option: Option?) {}
}