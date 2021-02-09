package chao.greenlabs.views.adpaters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.input.pointer.ConsumedData
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.viewmodels.ManageMarketViewModel

private const val ADD_CUSTOMER_VIEW = 0
private const val CUSTOMER_VIEW = 1

class CustomerAdapter(viewModel: ManageMarketViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val customerList = arrayListOf<CustomerData>()

    override fun getItemViewType(position: Int): Int {
        return if (customerList[position].customerId == null && customerList[position].soldDataList == null)
            ADD_CUSTOMER_VIEW else CUSTOMER_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_customer, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    fun setCustomerList(list: List<CustomerData>) {
        customerList.clear()
        customerList.addAll(list)

        notifyDataSetChanged()
    }
}