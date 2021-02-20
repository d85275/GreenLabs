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
import chao.greenlabs.utils.KeyboardUtils
import chao.greenlabs.viewmodels.AddCustomerSoldItemViewModel
import chao.greenlabs.viewmodels.AddCustomerViewModel
import chao.greenlabs.viewmodels.factories.AddCustomerVMFactory
import chao.greenlabs.views.adpaters.SearchedItemAdapter
import chao.greenlabs.views.adpaters.SoldItemAdapter
import kotlinx.android.synthetic.main.fragment_add_customer_solditem.*

class AddCustomerSoldItemFragment : BaseFragment() {

    private lateinit var viewModel: AddCustomerViewModel
    private lateinit var addCustomerSoldItemViewModel: AddCustomerSoldItemViewModel
    private lateinit var searchedAdapter: SearchedItemAdapter
    private lateinit var soldAdapter: SoldItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_add_customer_solditem)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel()
        setViews()
        registerObservers()
        setListeners()
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

        searchedAdapter = SearchedItemAdapter(viewModel, onClickedListener)
        rv_searched_items.layoutManager = LinearLayoutManager(requireContext())
        rv_searched_items.setHasFixedSize(true)
        rv_searched_items.adapter = searchedAdapter

        soldAdapter = SoldItemAdapter(viewModel)
        rv_sold_items.layoutManager = LinearLayoutManager(requireContext())
        rv_sold_items.setHasFixedSize(true)
        rv_sold_items.adapter = soldAdapter
    }

    private fun setListeners() {
        et_search.requestFocus()
        et_search.addTextChangedListener(textWatcher)

        KeyboardUtils.showKeyboard(requireContext(), et_search)

        ll_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun registerObservers() {
        viewModel.getMatchedItems().observe(viewLifecycleOwner, Observer { matchedList ->
            searchedAdapter.setList(matchedList)
        })

        addCustomerSoldItemViewModel.getClickedItem().observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            if (text.isEmpty()) {
                rv_sold_items.visibility = View.VISIBLE
                rv_searched_items.visibility = View.GONE
            } else {
                rv_sold_items.visibility = View.GONE
                rv_searched_items.visibility = View.VISIBLE
                viewModel.onSearch(s.toString())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}