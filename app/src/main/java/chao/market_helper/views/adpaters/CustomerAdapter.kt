package chao.market_helper.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chao.market_helper.databinding.ItemCustomerBinding
import chao.market_helper.datamodels.CustomerData
import chao.market_helper.viewmodels.ManageMarketViewModel
import chao.market_helper.views.adpaters.diffcallbacks.CustomerDataDiffCallback
import chao.market_helper.views.viewholders.CustomerViewHolder


class CustomerAdapter(
    private val viewModel: ManageMarketViewModel,
    private val onAddCustomerAction: ((customerData: CustomerData) -> Unit)
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val customerList = arrayListOf<CustomerData>()
    private lateinit var viewPool: RecyclerView.RecycledViewPool

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCustomerBinding.inflate(layoutInflater, parent, false)
        viewPool = RecyclerView.RecycledViewPool()
        return CustomerViewHolder(
            binding,
            viewModel
        )
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val customerData = customerList[holder.adapterPosition]
        (holder as CustomerViewHolder).bindView(
            viewPool,
            customerData,
            customerList.size,
            holder.adapterPosition
        ) { data -> onAddCustomerAction.invoke(data) }
    }

    fun setCustomerList(list: List<CustomerData>) {
        val diffCallback = CustomerDataDiffCallback(customerList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        customerList.clear()
        customerList.addAll(list)

        diffResult.dispatchUpdatesTo(this)
    }
}