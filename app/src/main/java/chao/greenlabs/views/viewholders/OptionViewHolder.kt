package chao.greenlabs.views.viewholders

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.databinding.ItemItemDetailsBinding
import chao.greenlabs.datamodels.ItemDetails
import chao.greenlabs.views.customedobjects.views.ItemRadioButton

class OptionViewHolder(private val binding: ItemItemDetailsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(details: ItemDetails) {
        binding.tvTitle.text = details.title
        for (i in details.details.indices) {
            addRadioButton(details.details[i])
        }
    }

    private fun addRadioButton(option: String) {
        val itemRadioButton = ItemRadioButton(binding.root.context)
        val price = "+ $0"
        itemRadioButton.setData(option, price)
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        itemRadioButton.layoutParams = layoutParams
        binding.rgDetails.addView(itemRadioButton)
    }
}