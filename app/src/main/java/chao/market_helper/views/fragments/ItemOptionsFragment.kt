package chao.market_helper.views.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.market_helper.R
import chao.market_helper.repository.Repository
import chao.market_helper.utils.ImageUtils
import chao.market_helper.viewmodels.AddCustomerViewModel
import chao.market_helper.viewmodels.ItemOptionsViewModel
import chao.market_helper.viewmodels.factories.AddCustomerVMFactory
import chao.market_helper.views.adpaters.addcustomer.ItemOptionsAdapter
import kotlinx.android.synthetic.main.fragment_item_options.*

class ItemOptionsFragment : BaseFragment() {

    private lateinit var viewModel: ItemOptionsViewModel
    private lateinit var addCustomerViewModel: AddCustomerViewModel

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
        val repository = Repository(requireContext())

        viewModel =
            ViewModelProvider(requireActivity()).get(ItemOptionsViewModel::class.java)

        val addCustomerFactory = AddCustomerVMFactory(repository)
        addCustomerViewModel =
            ViewModelProvider(
                requireActivity(),
                addCustomerFactory
            ).get(AddCustomerViewModel::class.java)
    }

    private fun registerObservers() {
        viewModel.getIsOptionsSelected().observe(viewLifecycleOwner, Observer { isOptionsSelected ->
            if (isOptionsSelected) {
                enableAddButton()
            } else {
                disableAddButton()
            }
        })
        viewModel.getTotalPrice().observe(viewLifecycleOwner, Observer { price ->
            tv_price.text = getString(R.string.price, price)
        })
    }

    private fun enableAddButton() {
        tv_add_item.alpha = 1f
        tv_add_item.setOnClickListener {
            addCustomerViewModel.onSearchItemClicked(viewModel.getSavedItem())
            findNavController().popBackStack(R.id.addCustomerFragment,false)
        }
    }

    private fun disableAddButton() {
        tv_add_item.alpha = 0.5f
        tv_add_item.setOnClickListener(null)
    }

    private fun setViews() {
        tv_name.text = viewModel.getItemData().name
        ImageUtils.loadImage(requireContext(), viewModel.getItemData().name, iv_image)

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