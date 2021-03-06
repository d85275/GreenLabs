package chao.greenlabs.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.databinding.FragmentAddCustomerSolditemBinding
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.AddCustomerSoldItemViewModel
import chao.greenlabs.viewmodels.AddCustomerViewModel
import chao.greenlabs.viewmodels.ItemOptionsViewModel
import chao.greenlabs.viewmodels.factories.AddCustomerVMFactory
import chao.greenlabs.views.adpaters.addcustomer.SearchedItemAdapter
import kotlinx.coroutines.launch

class AddCustomerSoldItemFragment : BaseFragment() {

    private lateinit var viewModel: AddCustomerViewModel
    private lateinit var itemOptionViewModel: ItemOptionsViewModel
    private lateinit var addCustomerSoldItemViewModel: AddCustomerSoldItemViewModel
    private lateinit var searchedAdapter: SearchedItemAdapter
    private lateinit var binding: FragmentAddCustomerSolditemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutInflater = LayoutInflater.from(requireContext())
        binding = FragmentAddCustomerSolditemBinding.inflate(layoutInflater, container, false)
        binding.searchedDataSize = 0
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
        val factory = AddCustomerVMFactory(Repository(requireContext()))
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(AddCustomerViewModel::class.java)

        addCustomerSoldItemViewModel =
            ViewModelProvider(this).get(AddCustomerSoldItemViewModel::class.java)

        itemOptionViewModel = ViewModelProvider(requireActivity()).get(ItemOptionsViewModel::class.java)
    }

    private fun setViews() {
        val onClickedListener: ((itemData: ItemData) -> Unit) = { item ->
            binding.etSearch.text.clear()
            //viewModel.onSearchItemClicked(item)
            Log.e("123","set item data")
            itemOptionViewModel.setItemData(item)
            addCustomerSoldItemViewModel.setClickedItem(item)
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

        binding.tvTitle.setOnClickListener {
            findNavController().navigate(R.id.action_addCustomerSoldItemFragment_to_itemOptionsFragment)
        }

        binding.tvAddItem.setOnClickListener {
            findNavController().navigate(R.id.action_addCustomerSoldItemFragment_to_addItemFragment)
        }
    }

    private fun registerObservers() {
        viewModel.getMatchedItems().observe(viewLifecycleOwner, Observer { matchedList ->
            binding.searchedDataSize = matchedList.size
            searchedAdapter.setList(matchedList)
            dismissLoading()
        })

        addCustomerSoldItemViewModel.getClickedItem().observe(viewLifecycleOwner, Observer {
            //findNavController().popBackStack()
            //Log.e("123","navigate to it")
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