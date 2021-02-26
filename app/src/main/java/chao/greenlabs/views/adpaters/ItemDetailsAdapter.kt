package chao.greenlabs.views.adpaters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.databinding.ItemItemDetailsBinding
import chao.greenlabs.datamodels.ItemDetails
import chao.greenlabs.views.customedobjects.views.ItemRadioButton
import chao.greenlabs.views.viewholders.OptionViewHolder
import kotlinx.android.synthetic.main.item_item_details.view.*

class ItemDetailsAdapter : RecyclerView.Adapter<OptionViewHolder>() {
    private val itemDetailList = arrayListOf<ItemDetails>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemItemDetailsBinding.inflate(inflater, parent, false)
        return OptionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemDetailList.size
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bindView(itemDetailList[position])

    }


    fun setList(list: List<ItemDetails>) {
        itemDetailList.clear()
        itemDetailList.addAll(list)
        notifyDataSetChanged()
    }
}