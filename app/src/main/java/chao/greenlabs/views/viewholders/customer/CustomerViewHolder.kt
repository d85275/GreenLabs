package chao.greenlabs.views.viewholders.customer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import chao.greenlabs.R
import chao.greenlabs.databinding.ItemCustomerBinding
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.viewmodels.ManageMarketViewModel
import com.bumptech.glide.Glide


class CustomerViewHolder(
    private val binding: ItemCustomerBinding, private val viewModel: ManageMarketViewModel
) : BaseViewHolder(binding.root) {

    override fun bindView(
        customerData: CustomerData?,
        position: Int,
        onAddCustomerAction: () -> Unit
    ) {
        super.bindView(customerData, position, onAddCustomerAction)
        setViews(customerData, position, viewModel)
        setListeners()
    }

    private fun setViews(
        customerData: CustomerData?,
        position: Int,
        viewModel: ManageMarketViewModel
    ) {
        if (customerData?.soldDataList == null) return

        val context = binding.root.context

        val customerNo = context.getString(R.string.customer_no, position + 1)
        val total = context.getString(R.string.price, customerData.total.toString())

        binding.tvCustomerNumber.text = customerNo
        binding.tvTotalPrice.text = total

        binding.llCustomers.removeAllViews()
        customerData.soldDataList?.forEach { soldItem ->
            addView(soldItem, viewModel)
        }

    }

    private fun addView(soldItem: CustomerData.SoldItem, viewModel: ManageMarketViewModel) {
        val name = soldItem.name
        val context = binding.root.context
        val price = soldItem.price
        val count = soldItem.count
        val image = viewModel.getImage(name, price)

        val itemView: View =
            LayoutInflater.from(binding.root.context)
                .inflate(R.layout.view_customer, binding.llCustomers, false)

        val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        val tvPrice = itemView.findViewById<TextView>(R.id.tv_price)
        val tvCount = itemView.findViewById<TextView>(R.id.tv_count)
        val ivImage = itemView.findViewById<ImageView>(R.id.iv_image)

        tvName.text = name
        tvPrice.text = context.getString(R.string.price, price)
        tvCount.text = count.toString()
        Glide.with(binding.root.context).load(image).into(ivImage)

        binding.llCustomers.addView(itemView)
    }

    private fun setListeners() {

    }
}