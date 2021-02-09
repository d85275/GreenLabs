package chao.greenlabs.views.viewholders.customer

import android.view.View
import chao.greenlabs.databinding.ItemAddCustomerBinding
import chao.greenlabs.datamodels.CustomerData

class AddCustomerViewHolder(private val binding: ItemAddCustomerBinding) :
    BaseViewHolder(binding.root) {

    override fun bindView(customerData: CustomerData?, onAddCustomerAction: () -> Unit) {
        super.bindView(customerData, onAddCustomerAction)
        setListeners()
    }

    fun setListeners() {
        binding.llAddCustomer.setOnClickListener {
            onAddCustomerAction.invoke()
        }
    }

}