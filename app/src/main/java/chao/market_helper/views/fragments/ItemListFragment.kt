package chao.market_helper.views.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import chao.market_helper.R
import chao.market_helper.datamodels.ItemData
import chao.market_helper.repository.Repository
import chao.market_helper.utils.DialogUtils
import chao.market_helper.views.customedobjects.SwipeCallback
import chao.market_helper.viewmodels.AddItemViewModel
import chao.market_helper.viewmodels.factories.ItemListVMFactory
import chao.market_helper.viewmodels.ItemListViewModel
import chao.market_helper.viewmodels.factories.AddItemVMFactory
import chao.market_helper.views.adpaters.item.ItemAdapter
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.fragment_item_list.ll_add

private const val SCROLL_DELAY_MS = 150L

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

        val addItemFactory = AddItemVMFactory(resources, repository)
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

        adapter = ItemAdapter(onItemClickedAction)
        rv_items.layoutManager = LinearLayoutManager(requireContext())
        rv_items.setHasFixedSize(true)
        rv_items.adapter = adapter
    }

    private fun registerObservers() {
        listViewModel.getItemList().observe(viewLifecycleOwner, Observer { list ->
            dismissLoading()
            scrollToTop()
            adapter.setList(list)
            if (list.isEmpty()) {
                no_items.visibility = View.VISIBLE
                rv_items.visibility = View.GONE
            } else {
                no_items.visibility = View.GONE
                rv_items.visibility = View.VISIBLE
            }
        })
    }

    private fun scrollToTop() {
        if (addItemViewModel.getIsItemAdded()) {
            Handler(Looper.getMainLooper()).postDelayed(
                { rv_items.smoothScrollToPosition(0) }, SCROLL_DELAY_MS
            )
            addItemViewModel.setIsItemAdd(false)
        }
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