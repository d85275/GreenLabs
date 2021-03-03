package chao.greenlabs.views.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.views.customedobjects.SwipeCallback
import chao.greenlabs.viewmodels.AddItemViewModel
import chao.greenlabs.viewmodels.factories.ItemListVMFactory
import chao.greenlabs.viewmodels.ItemListViewModel
import chao.greenlabs.viewmodels.factories.AddItemVMFactory
import chao.greenlabs.views.adpaters.additem.ItemAdapter
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.fragment_item_list.ll_add

class ItemListFragment : BaseFragment() {

    private lateinit var listViewModel: ItemListViewModel
    private lateinit var addItemViewModel: AddItemViewModel
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_item_list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Repository(requireContext())
        showLoading()
        getViewModel()
        setViews()
        registerObservers()
        setListeners()
        listViewModel.loadItemData()
    }

    private fun getViewModel() {
        val repository = Repository(requireContext())
        val factory = ItemListVMFactory(repository)
        listViewModel = ViewModelProvider(this, factory).get(ItemListViewModel::class.java)

        val addItemFactory = AddItemVMFactory(repository)
        addItemViewModel =
            ViewModelProvider(requireActivity(), addItemFactory).get(AddItemViewModel::class.java)
    }

    private fun setViews() {
        val onItemClickedAction: ((itemData: ItemData) -> Unit) = { itemData ->
            addItemViewModel.setUpdatedItem(itemData)
            findNavController().navigate(R.id.action_itemListFragment_to_addItemFragment)
        }

        val itemTouchHelper =
            ItemTouchHelper(SwipeCallback { position ->
                onItemSwipedAction.invoke(position)
            })
        itemTouchHelper.attachToRecyclerView(rv_items)

        adapter =
            ItemAdapter(onItemClickedAction)
        rv_items.layoutManager = LinearLayoutManager(requireContext())
        rv_items.setHasFixedSize(true)
        rv_items.adapter = adapter
    }

    private fun registerObservers() {
        listViewModel.getItemList().observe(viewLifecycleOwner, Observer { list ->
            dismissLoading()
            adapter.setList(list)
        })
    }

    private fun setListeners() {
        ll_back.setOnClickListener {
            findNavController().popBackStack()
        }

        ll_add.setOnClickListener {
            findNavController().navigate(R.id.action_itemListFragment_to_addItemFragment)
        }
    }

    private val onItemSwipedAction: ((position: Int) -> Unit) = { position ->
        val confirmAction: (() -> Unit) = {
            listViewModel.deleteItem(position)
        }
        val cancelAction: (() -> Unit) = {
            adapter.notifyDataSetChanged()
        }
        DialogUtils.showDelete(requireContext(), confirmAction, cancelAction)
    }
}