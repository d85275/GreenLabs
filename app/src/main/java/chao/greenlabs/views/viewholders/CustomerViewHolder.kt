package chao.greenlabs.views.viewholders

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.databinding.ItemCustomerBinding
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.views.adpaters.addcustomer.CustomerSoldItemAdapter

class CustomerViewHolder(
    private val binding: ItemCustomerBinding,
    private val viewModel: ManageMarketViewModel
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var customerData: CustomerData
    private lateinit var context: Context
    private lateinit var onAddCustomerAction: ((customerData: CustomerData) -> Unit)
    private lateinit var viewPool: RecyclerView.RecycledViewPool

    fun bindView(
        viewPool: RecyclerView.RecycledViewPool,
        customerData: CustomerData,
        position: Int,
        onAddCustomerAction: (customerData: CustomerData) -> Unit
    ) {
        this.viewPool = viewPool
        this.customerData = customerData
        this.context = binding.root.context
        this.onAddCustomerAction = onAddCustomerAction
        setViews(position)
        setListeners()
        setSoldItems()
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

    private fun setSoldItems(){
        val adapter =
            CustomerSoldItemAdapter(
                Repository(context)
            )
        val list = customerData.soldDataList ?: listOf<CustomerData.SoldItem>()
        adapter.setList(list)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager.initialPrefetchItemCount = 3
        binding.rvSoldItems.layoutManager = layoutManager
        binding.rvSoldItems.setHasFixedSize(true)
        binding.rvSoldItems.setItemViewCacheSize(20)
        binding.rvSoldItems.adapter = adapter
        binding.rvSoldItems.setRecycledViewPool(viewPool)
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
}