package chao.greenlabs.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.AnimUtils
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.utils.TouchCallbackUtils
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.viewmodels.factories.MarketListVMFactory
import chao.greenlabs.viewmodels.MarketListViewModel
import chao.greenlabs.viewmodels.factories.ManageMarketVMFactory
import chao.greenlabs.views.adpaters.MarketAdapter
import kotlinx.android.synthetic.main.fragment_market_list.*

class MarketListFragment : BaseFragment() {

    private lateinit var listViewModel: MarketListViewModel
    private lateinit var manageMarketViewModel: ManageMarketViewModel
    private lateinit var adapter: MarketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_market_list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Repository(requireContext())
        showLoading()
        getViewModel()
        setViews()
        registerObservers()
        setListeners()
        loadData()
        AnimUtils.showMarketListDetail(cl_parent, cl_market_detail)
    }

    override fun onDestroy() {
        super.onDestroy()
        manageMarketViewModel.clearMarketSoldData()
    }

    private fun getViewModel() {
        val repository = Repository(requireContext())
        val factory = MarketListVMFactory(repository)
        listViewModel = ViewModelProvider(this, factory).get(MarketListViewModel::class.java)

        val manageMarketVMFactory = ManageMarketVMFactory(resources, repository)
        manageMarketViewModel = ViewModelProvider(
            requireActivity(),
            manageMarketVMFactory
        ).get(ManageMarketViewModel::class.java)

    }

    private fun setViews() {
        val onClickedListener: ((data: MarketData) -> Unit) = { data ->
            manageMarketViewModel.setMarketData(data)
            findNavController().navigate(R.id.action_marketListFragment_to_manageMarketFragment)
        }

        val itemTouchHelper =
            ItemTouchHelper(TouchCallbackUtils.getItemTouchHelperCallback { position ->
                onItemSwipedAction.invoke(position)
            })
        itemTouchHelper.attachToRecyclerView(rv_markets)

        adapter = MarketAdapter(onClickedListener)
        rv_markets.layoutManager = LinearLayoutManager(requireContext())
        rv_markets.setHasFixedSize(true)
        rv_markets.adapter = adapter
    }

    private val onItemSwipedAction: ((position: Int) -> Unit) = { position ->
        val confirmAction: (() -> Unit) = {
            listViewModel.deleteMarket(position)
        }
        val cancelAction: (() -> Unit) = {
            adapter.notifyDataSetChanged()
        }
        DialogUtils.showDelete(requireContext(), confirmAction, cancelAction)
    }

    private fun registerObservers() {
        listViewModel.getMarketList().observe(viewLifecycleOwner, Observer { list ->
            dismissLoading()
            adapter.setList(list)
            tv_market_count.text = getString(R.string.joined_market, list.size)
            if (list.isNotEmpty()) {
                tv_joined_markets.visibility = View.VISIBLE
                rv_markets.visibility = View.VISIBLE
                ll_no_market.visibility = View.GONE
            } else {
                ll_no_market.visibility = View.VISIBLE
                rv_markets.visibility = View.GONE
                tv_joined_markets.visibility = View.GONE
            }
        })

        listViewModel.getTotalIncome().observe(viewLifecycleOwner, Observer { income ->
            tv_market_income.text = getString(R.string.total_market_income, income)
        })
    }

    private fun setListeners() {
        ll_add_items.setOnClickListener {
            findNavController().navigate(R.id.action_marketListFragment_to_itemListFragment)
        }

        ll_add.setOnClickListener {
            findNavController().navigate(R.id.action_marketListFragment_to_addMarketFragment)
        }
    }

    private fun loadData() {
        listViewModel.loadMarketData()
    }
}