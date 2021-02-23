package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.databinding.ItemCustomerBinding
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.views.adpaters.diffcallbacks.CustomerDataDiffCallback
import chao.greenlabs.views.viewholders.customer.CustomerViewHolder


class CustomerAdapter(
    private val viewModel: ManageMarketViewModel,
    private val onAddCustomerAction: ((customerData: CustomerData) -> Unit)
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val customerList = arrayListOf<CustomerData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCustomerBinding.inflate(layoutInflater, parent, false)
        return CustomerViewHolder(binding, viewModel)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val customerData = customerList[holder.adapterPosition]
        (holder as CustomerViewHolder).bindView(
            customerData,
            holder.adapterPosition
        ) { data -> onAddCustomerAction.invoke(data) }
    }

    fun setCustomerList(list: List<CustomerData>) {
        val diffCallback =
            CustomerDataDiffCallback(
                list,
                customerList
            )
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        customerList.clear()
        customerList.addAll(list)

        //diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}