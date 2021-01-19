package chao.greenlabs.views

import android.os.Bundle
import android.util.Log
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
import chao.greenlabs.viewmodels.AddItemViewModel
import chao.greenlabs.viewmodels.factories.ItemListVMFactory
import chao.greenlabs.viewmodels.ItemListViewModel
import chao.greenlabs.viewmodels.factories.AddItemVMFactory
import chao.greenlabs.views.adpaters.ItemAdapter
import kotlinx.android.synthetic.main.fragment_item_list.*

class ItemListFragment : Fragment() {

    private lateinit var listViewModel: ItemListViewModel
    private lateinit var addItemViewModel: AddItemViewModel
    private lateinit var adapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Repository(requireContext())
        getViewModel()
        setViews()
        registerObservers()
        listViewModel.loadItemData()
    }

    private fun getViewModel() {
        val repository = Repository(requireContext())
        val factory = ItemListVMFactory(repository)
        listViewModel = ViewModelProvider(this, factory).get(ItemListViewModel::class.java)

        val addItemFactory = AddItemVMFactory(repository)
        addItemViewModel = ViewModelProvider(requireActivity(), addItemFactory).get(AddItemViewModel::class.java)
    }

    private fun setViews() {
        val onItemClickedAction: ((itemData: ItemData) -> Unit) = { itemData ->
            addItemViewModel.setUpdatedItem(itemData)
            findNavController().navigate(R.id.action_itemListFragment_to_addItemFragment)
        }
        adapter = ItemAdapter(listViewModel, onItemClickedAction)
        rv_items.layoutManager = LinearLayoutManager(requireContext())
        rv_items.setHasFixedSize(true)
        rv_items.adapter = adapter
    }

    private fun registerObservers() {
        listViewModel.getItemList().observe(viewLifecycleOwner, Observer { list ->
            adapter.setList(list)
        })
    }
}