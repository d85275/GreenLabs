package chao.greenlabs.views.viewholders.customer

import chao.greenlabs.databinding.ItemAddCustomerBinding
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.viewmodels.ManageMarketViewModel

class AddCustomerViewHolder(
    private val binding: ItemAddCustomerBinding,
    private val viewModel: ManageMarketViewModel
) :
    BaseViewHolder(binding.root) {

    private lateinit var customerData: CustomerData

    override fun bindView(
        customerData: CustomerData,
        position: Int,
        onAddCustomerAction: (customerData: CustomerData) -> Unit
    ) {
        super.bindView(customerData, position, onAddCustomerAction)
        this.customerData = customerData
        setListeners()
    }

    fun setListeners() {
        binding.clAddCustomer.setOnClickListener {
            val marketId = viewModel.getMarketData().value?.id!!
            onAddCustomerAction.invoke(CustomerData.createNewCustomer(marketId))
        }
    }

}