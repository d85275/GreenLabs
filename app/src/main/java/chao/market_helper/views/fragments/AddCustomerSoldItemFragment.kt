package chao.market_helper.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.market_helper.R
import chao.market_helper.databinding.FragmentAddCustomerSolditemBinding
import chao.market_helper.datamodels.ItemData
import chao.market_helper.repository.Repository
import chao.market_helper.viewmodels.AddCustomerViewModel
import chao.market_helper.viewmodels.ItemOptionsViewModel
import chao.market_helper.viewmodels.factories.AddCustomerVMFactory
import chao.market_helper.views.adpaters.addcustomer.SearchedItemAdapter

class AddCustomerSoldItemFragment : BaseFragment() {

    private lateinit var viewModel: AddCustomerViewModel
    private lateinit var itemOptionViewModel: ItemOptionsViewModel

    private lateinit var searchedAdapter: SearchedItemAdapter
    private lateinit var binding: FragmentAddCustomerSolditemBinding
    private var isSearched = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = LayoutInflater.from(requireContext())
        binding = FragmentAddCustomerSolditemBinding.inflate(layoutInflater, container, false)
        binding.searchedDataSize = 0
        isSearched = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        getViewModel()
        setViews()
        registerObservers()
        setListeners()
        viewModel.loadItemData()
    }

    override fun onResume() {
        super.onResume()
        binding.etSearch.setText("")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetData()
    }

    private fun getViewModel() {
        val repository = Repository(requireContext())
        val factory = AddCustomerVMFactory(repository)
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(AddCustomerViewModel::class.java)

        itemOptionViewModel = ViewModelProvider(requireActivity())
            .get(ItemOptionsViewModel::class.java)
    }

    private fun setViews() {
        val onClickedListener: ((itemData: ItemData) -> Unit) = { item ->
            binding.etSearch.text.clear()
            itemOptionViewModel.setItemData(item)
            findNavController().navigate(R.id.action_addCustomerSoldItemFragment_to_itemOptionsFragment)
        }

        searchedAdapter = SearchedItemAdapter(onClickedListener)
        binding.rvSearchedItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchedItems.setHasFixedSize(true)
        binding.rvSearchedItems.adapter = searchedAdapter
    }

    private fun setListeners() {
        binding.etSearch.addTextChangedListener(textWatcher)

        binding.llBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvAddItem.setOnClickListener {
            findNavController().navigate(R.id.action_addCustomerSoldItemFragment_to_addItemFragment)
        }
    }

    private fun registerObservers() {
        viewModel.getMatchedItems().observe(viewLifecycleOwner, Observer { matchedList ->
            binding.searchedDataSize = matchedList.size
            if (isSearched && matchedList.isEmpty()) {
                binding.llNotFound.visibility = View.VISIBLE
            } else {
                binding.llNotFound.visibility = View.GONE
            }
            searchedAdapter.setList(matchedList)
            dismissLoading()
        })
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            viewModel.onSearch(s.toString().trim())
            if (s.toString().isNotEmpty()) isSearched = true
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
}