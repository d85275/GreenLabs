package chao.greenlabs.views.viewholders.customer

import androidx.viewpager2.widget.ViewPager2
import chao.greenlabs.R
import chao.greenlabs.databinding.ItemCustomerBinding
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.views.SoldItemTransformer
import chao.greenlabs.views.adpaters.CustomerSoldItemAdapter

private const val OFFSET_LIMIT = 6

class CustomerViewHolder(
    private val binding: ItemCustomerBinding, private val viewModel: ManageMarketViewModel
) : BaseViewHolder(binding.root) {

    override fun bindView(
        customerData: CustomerData?,
        position: Int,
        onAddCustomerAction: () -> Unit
    ) {
        super.bindView(customerData, position, onAddCustomerAction)
        setViews(customerData, position)
        setListeners()
        setViewPager(customerData, viewModel)
    }

    private fun setViewPager(customerData: CustomerData?, viewModel: ManageMarketViewModel) {
        val adapter = CustomerSoldItemAdapter(viewModel)
        val list = customerData?.soldDataList ?: listOf<CustomerData.SoldItem>()
        adapter.setList(list)

        binding.vgSoldItems.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vgSoldItems.adapter = adapter
        binding.vgSoldItems.offscreenPageLimit = 6
        binding.vgSoldItems.setPageTransformer(SoldItemTransformer(OFFSET_LIMIT))
    }

    private fun setViews(customerData: CustomerData?, position: Int) {
        if (customerData?.soldDataList == null) return

        val context = binding.root.context
        binding.memo = customerData.memo
        binding.customerNo = context.getString(R.string.customer_no, position + 1)
        binding.total = context.getString(R.string.price, customerData.total.toString())

    }

    private fun setListeners() {
        binding.root.setOnClickListener {

        }
    }
}