package chao.greenlabs.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.AddCustomerSoldItemViewModel
import chao.greenlabs.viewmodels.AddCustomerViewModel
import chao.greenlabs.viewmodels.factories.AddCustomerVMFactory
import chao.greenlabs.views.adpaters.addcustomer.SearchedItemAdapter
import kotlinx.android.synthetic.main.fragment_add_customer_solditem.*

class AddCustomerSoldItemFragment : BaseFragment() {

    private lateinit var viewModel: AddCustomerViewModel
    private lateinit var addCustomerSoldItemViewModel: AddCustomerSoldItemViewModel
    private lateinit var searchedAdapter: SearchedItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_add_customer_solditem)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        getViewModel()
        setViews()
        registerObservers()
        setListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetData()
    }

    private fun getViewModel() {
        val factory = AddCustomerVMFactory(Repository(requireContext()))
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(AddCustomerViewModel::class.java)

        addCustomerSoldItemViewModel =
            ViewModelProvider(this).get(AddCustomerSoldItemViewModel::class.java)
    }

    private fun setViews() {
        val onClickedListener: ((itemData: ItemData) -> Unit) = { item ->
            et_search.text.clear()
            viewModel.onSearchItemClicked(item)
            addCustomerSoldItemViewModel.setClickedItem(item)
        }

        searchedAdapter =
            SearchedItemAdapter(
                onClickedListener
            )
        rv_searched_items.layoutManager = LinearLayoutManager(requireContext())
        rv_searched_items.setHasFixedSize(true)
        rv_searched_items.adapter = searchedAdapter
    }

    private fun setListeners() {
        et_search.addTextChangedListener(textWatcher)

        ll_back.setOnClickListener {
            findNavController().popBackStack()
        }

        tv_title.setOnClickListener {
            findNavController().navigate(R.id.action_addCustomerSoldItemFragment_to_soldItemDetailsFragment)
        }
    }

    private fun registerObservers() {
        viewModel.getMatchedItems().observe(viewLifecycleOwner, Observer { matchedList ->
            searchedAdapter.setList(matchedList)
            dismissLoading()
        })

        addCustomerSoldItemViewModel.getClickedItem().observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            viewModel.onSearch(s.toString().trim())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}