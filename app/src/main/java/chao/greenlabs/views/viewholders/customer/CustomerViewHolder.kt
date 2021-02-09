package chao.greenlabs.views.viewholders.customer

import chao.greenlabs.databinding.ItemCustomerBinding
import chao.greenlabs.datamodels.CustomerData

class CustomerViewHolder(binding: ItemCustomerBinding) : BaseViewHolder(binding.root) {

    override fun bindView(customerData: CustomerData?, onAddCustomerAction: () -> Unit) {
        super.bindView(customerData, onAddCustomerAction)
        setListeners()
    }

    fun setListeners() {

    }
}