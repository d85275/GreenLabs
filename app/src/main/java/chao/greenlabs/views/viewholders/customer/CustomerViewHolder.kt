package chao.greenlabs.views.viewholders.customer

import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import chao.greenlabs.R
import chao.greenlabs.databinding.ItemCustomerBinding
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.views.SoldItemTransformer
import chao.greenlabs.views.adpaters.CustomerSoldItemAdapter

private const val OFFSET_LIMIT = 6

class CustomerViewHolder(
    private val binding: ItemCustomerBinding,
    private val viewModel: ManageMarketViewModel
) : BaseViewHolder(binding.root) {

    private lateinit var customerData: CustomerData

    override fun bindView(
        customerData: CustomerData,
        position: Int,
        onAddCustomerAction: (customerData: CustomerData) -> Unit
    ) {
        super.bindView(customerData, position, onAddCustomerAction)
        this.customerData = customerData
        setViews(position)
        setListeners()
        setViewPager(viewModel)
    }

    private fun setViews(position: Int) {
        if (customerData.soldDataList == null) return

        val context = binding.root.context
        val subTotal = customerData.total
        val discount = customerData.discount
        val total = subTotal - discount

        binding.memo = customerData.memo
        binding.customerNo = context.getString(R.string.customer_no, position + 1)
        binding.subTotal = context.getString(R.string.price, subTotal.toString())
        binding.discount = context.getString(R.string.price, discount.toString())
        binding.total = context.getString(R.string.price, total.toString())
    }

    private fun setListeners() {
        binding.llEdit.setOnClickListener {
            onAddCustomerAction.invoke(customerData)
        }
    }

    private fun setViewPager(viewModel: ManageMarketViewModel) {
        val adapter = CustomerSoldItemAdapter(viewModel)
        val list = customerData.soldDataList ?: listOf<CustomerData.SoldItem>()
        adapter.setList(list)

        binding.vgSoldItems.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vgSoldItems.adapter = adapter
        binding.vgSoldItems.offscreenPageLimit = 6
        binding.vgSoldItems.setPageTransformer(SoldItemTransformer(OFFSET_LIMIT))
    }
}