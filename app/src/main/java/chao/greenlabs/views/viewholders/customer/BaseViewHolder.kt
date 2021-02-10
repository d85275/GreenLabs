package chao.greenlabs.views.viewholders.customer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.datamodels.CustomerData

open class BaseViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    lateinit var onAddCustomerAction: (() -> Unit)

    open fun bindView(
        customerData: CustomerData?,
        position: Int,
        onAddCustomerAction: (() -> Unit)
    ) {
        this.onAddCustomerAction = onAddCustomerAction
    }
}