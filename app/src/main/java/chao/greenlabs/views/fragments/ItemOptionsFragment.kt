package chao.greenlabs.views.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.BitmapUtils
import chao.greenlabs.viewmodels.ItemOptionsViewModel
import chao.greenlabs.viewmodels.factories.MarketListVMFactory
import chao.greenlabs.views.adpaters.addcustomer.ItemOptionsAdapter
import kotlinx.android.synthetic.main.fragment_item_options.*

class ItemOptionsFragment : BaseFragment() {

    private lateinit var viewModel: ItemOptionsViewModel
    private lateinit var adapter: ItemOptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_item_options)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModels()
        setViews()
        setListeners()
        registerObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }

    private fun getViewModels() {
        val factory = MarketListVMFactory(Repository(requireContext()))
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(ItemOptionsViewModel::class.java)
    }

    private fun registerObservers() {
        viewModel.getIsOptionsSelected().observe(viewLifecycleOwner, Observer { isOptionsSelected ->
            if (isOptionsSelected) {
                enableAddButton()
            } else {
                disableAddButton()
            }
        })
    }

    private fun enableAddButton() {
        tv_add_item.alpha = 1f
        tv_add_item.setOnClickListener {
            viewModel.save()
        }
    }

    private fun disableAddButton() {
        tv_add_item.alpha = 0.5f
        tv_add_item.setOnClickListener(null)
    }

    private fun setViews() {
        tv_name.text = viewModel.getItemData().name
        tv_price.text = viewModel.getItemData().price
        val bitmap =
            viewModel.loadBitmap(viewModel.getItemData().name, viewModel.getItemData().price)
        BitmapUtils.loadBitmap(requireContext(), bitmap, iv_image)

        adapter = ItemOptionsAdapter(
            viewModel
        )
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_details.setHasFixedSize(true)
        rv_details.layoutManager = layoutManager
        rv_details.adapter = adapter
        val categoryList = viewModel.getCategoryList()
        adapter.setList(categoryList)
        if (categoryList.isEmpty()) {
            enableAddButton()
        }
    }

    private fun setListeners() {
        ll_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}