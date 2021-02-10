package chao.greenlabs.views.viewholders.customer

import chao.greenlabs.databinding.ItemAddCustomerBinding
import chao.greenlabs.datamodels.CustomerData

class AddCustomerViewHolder(private val binding: ItemAddCustomerBinding) :
    BaseViewHolder(binding.root) {

    override fun bindView(
        customerData: CustomerData?,
        position: Int,
        onAddCustomerAction: () -> Unit
    ) {
        super.bindView(customerData, position, onAddCustomerAction)
        setListeners()
    }

    fun setListeners() {
        binding.llAddCustomer.setOnClickListener {
            onAddCustomerAction.invoke()
        }
    }

}