package chao.greenlabs.views.viewholders.customer

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import chao.greenlabs.R
import chao.greenlabs.databinding.ItemCustomerBinding
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.views.customedobjects.SoldItemTransformer
import chao.greenlabs.views.adpaters.CustomerSoldItemAdapter


private const val OFFSET_LIMIT = 6

class CustomerViewHolder(
    private val binding: ItemCustomerBinding,
    private val viewModel: ManageMarketViewModel
) :  RecyclerView.ViewHolder(binding.root) {
    private lateinit var customerData: CustomerData
    private lateinit var context: Context
    private lateinit var onAddCustomerAction: ((customerData: CustomerData) -> Unit)

    fun bindView(
        customerData: CustomerData,
        position: Int,
        onAddCustomerAction: (customerData: CustomerData) -> Unit
    ) {
        this.customerData = customerData
        this.context = binding.root.context
        this.onAddCustomerAction = onAddCustomerAction
        setViews(position)
        setListeners()
        setViewPager(viewModel)
    }

    private fun setViews(position: Int) {
        if (customerData.soldDataList == null) return

        val subTotal = customerData.total
        val discount = customerData.discount
        val total = subTotal - discount

        binding.memo = "${customerData.memo}\u0020"
        binding.customerNo = context.getString(R.string.customer_no, position + 1)
        binding.subTotal = context.getString(R.string.price, subTotal.toString())
        binding.discount = context.getString(R.string.price, discount.toString())
        binding.total = context.getString(R.string.price, total.toString())
    }

    private fun setListeners() {
        binding.llEdit.setOnClickListener {
            showPopup(it)
        }
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(context, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.customer_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            itemClicked(item.itemId)
            true
        }
        popup.show()
    }

    private fun itemClicked(id: Int) {
        when (id) {
            R.id.edit -> {
                onAddCustomerAction.invoke(customerData)
            }
            R.id.delete -> {
                DialogUtils.showDelete(context) {
                    viewModel.deleteCustomer(customerData)
                }
            }
            else -> {
            }
        }
    }

    private fun setViewPager(viewModel: ManageMarketViewModel) {
        val adapter = CustomerSoldItemAdapter(viewModel)
        val list = customerData.soldDataList ?: listOf<CustomerData.SoldItem>()
        adapter.setList(list)

        binding.vgSoldItems.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vgSoldItems.adapter = adapter
        binding.vgSoldItems.offscreenPageLimit = 6
        binding.vgSoldItems.setPageTransformer(
            SoldItemTransformer(
                OFFSET_LIMIT
            )
        )
    }
}