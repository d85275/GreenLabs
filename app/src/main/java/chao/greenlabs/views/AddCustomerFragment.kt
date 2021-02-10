package chao.greenlabs.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.viewmodels.AddCustomerViewModel
import chao.greenlabs.viewmodels.factories.AddCustomerVMFactory
import chao.greenlabs.views.adpaters.SearchedItemAdapter
import chao.greenlabs.views.adpaters.SoldItemAdapter
import kotlinx.android.synthetic.main.fragment_add_customer.*

class AddCustomerFragment : Fragment() {

    private lateinit var viewModel: AddCustomerViewModel
    private lateinit var searchedAdapter: SearchedItemAdapter
    private lateinit var soldAdapter: SoldItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_customer, container, false)
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
    }

    private fun setViews() {
        val onClickedListener: ((itemData: ItemData) -> Unit) = { item ->
            et_search.text.clear()
            viewModel.onSearchItemClicked(item)
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
        et_search.addTextChangedListener(textWatcher)

        ll_add.setOnClickListener {
            viewModel.saveCustomer(et_memo.text.toString())
        }

        ll_back.setOnClickListener {
            val total = viewModel.getCustomerData().value?.total
            if (total == null || total.toInt() == 0) {
                findNavController().popBackStack()
            } else {
                showBackWarningDialog()
            }
        }
    }

    private fun showBackWarningDialog() {
        val msg = getString(R.string.back_warning)
        DialogUtils.showQuestion(requireContext(), msg) { findNavController().popBackStack() }
    }

    private fun registerObservers() {
        viewModel.getMatchedItems().observe(viewLifecycleOwner, Observer { matchedList ->
            searchedAdapter.setList(matchedList)
        })


        viewModel.getCustomerData().observe(viewLifecycleOwner, Observer { customerData ->
            val list = customerData.soldDataList ?: return@Observer
            soldAdapter.setItem(list)
            tv_total.text = customerData.total.toString()
        })

        viewModel.getIsCustomerSaved().observe(viewLifecycleOwner, Observer { isCustomerSaved ->
            if (isCustomerSaved) {
                findNavController().popBackStack()
                viewModel.setIsCustomerSaved(false)
            }
        })
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            if (text.isEmpty()) {
                tv_sold_item.visibility = View.VISIBLE
                rv_sold_items.visibility = View.VISIBLE
                et_memo.visibility = View.VISIBLE
                rv_searched_items.visibility = View.GONE
            } else {
                tv_sold_item.visibility = View.GONE
                rv_sold_items.visibility = View.GONE
                et_memo.visibility = View.GONE
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