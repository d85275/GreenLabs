package chao.greenlabs.views.viewholders

import android.util.Log
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.databinding.ItemItemDetailsBinding
import chao.greenlabs.datamodels.ItemDetails
import chao.greenlabs.viewmodels.ItemOptionsViewModel

private const val RATE = 30

class OptionViewHolder(private val binding: ItemItemDetailsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var details: ItemDetails
    private lateinit var viewModel: ItemOptionsViewModel
    private var mPosition = -1

    fun bindView(
        details: ItemDetails,
        position: Int,
        viewModel: ItemOptionsViewModel
    ) {
        this.details = details
        this.viewModel = viewModel
        this.mPosition = position
        setViews()
        setListeners()
    }

    private fun setViews() {
        binding.tvTitle.text = details.title
        for (i in details.details.indices) {
            addRadioButton(details.details[i])
        }
    }

    private fun addRadioButton(option: String) {
        val itemRadioButton = AppCompatRadioButton(binding.root.context)
        itemRadioButton.text = option
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        itemRadioButton.layoutParams = layoutParams
        binding.rgDetails.addView(itemRadioButton)
    }

    private var checkedId: Int? = null

    private fun setListeners() {
        binding.rgDetails.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = binding.rgDetails.findViewById<AppCompatRadioButton>(checkedId)
            val option = radioButton.text.toString()
            viewModel.setSelection(mPosition, option)
        }
    }
}