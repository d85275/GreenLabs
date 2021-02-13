package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.databinding.ItemAddCustomerBinding
import chao.greenlabs.databinding.ItemCustomerBinding
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.views.viewholders.customer.AddCustomerViewHolder
import chao.greenlabs.views.viewholders.customer.BaseViewHolder
import chao.greenlabs.views.viewholders.customer.CustomerViewHolder

private const val ADD_CUSTOMER_VIEW = 0
private const val CUSTOMER_VIEW = 1

class CustomerAdapter(
    private val viewModel: ManageMarketViewModel,
    private val onAddCustomerAction: ((customerData: CustomerData) -> Unit)
) :
    RecyclerView.Adapter<BaseViewHolder>() {

    private val customerList = arrayListOf<CustomerData>()

    override fun getItemViewType(position: Int): Int {
        return if (customerList[position].soldDataList == null)
            ADD_CUSTOMER_VIEW else CUSTOMER_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == ADD_CUSTOMER_VIEW) {
            val binding = ItemAddCustomerBinding.inflate(layoutInflater, parent, false)
            AddCustomerViewHolder(binding, viewModel)
        } else {
            val binding = ItemCustomerBinding.inflate(layoutInflater, parent, false)
            CustomerViewHolder(binding, viewModel)
        }
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val customerData = customerList[position]
        holder.bindView(
            customerData,
            position
        ) { data -> onAddCustomerAction.invoke(data) }
    }

    fun setCustomerList(list: List<CustomerData>) {
        customerList.clear()
        customerList.addAll(list)

        notifyDataSetChanged()
    }
}