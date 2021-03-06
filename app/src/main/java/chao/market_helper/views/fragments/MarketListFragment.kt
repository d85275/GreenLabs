package chao.market_helper.views.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import chao.market_helper.R
import chao.market_helper.datamodels.MarketData
import chao.market_helper.repository.Repository
import chao.market_helper.utils.AnimUtils
import chao.market_helper.utils.DialogUtils
import chao.market_helper.views.customedobjects.SwipeCallback
import chao.market_helper.viewmodels.ManageMarketViewModel
import chao.market_helper.viewmodels.factories.MarketListVMFactory
import chao.market_helper.viewmodels.MarketListViewModel
import chao.market_helper.viewmodels.factories.ManageMarketVMFactory
import chao.market_helper.views.MainActivity
import chao.market_helper.views.adpaters.MarketAdapter
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
        showLoading()
        getViewModel()
        setViews()
        registerObservers()
        setListeners()
        loadData()
        AnimUtils.showFromLeft(cl_parent, cl_market_detail)
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
            ItemTouchHelper(SwipeCallback { position ->
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
        setOnBackPressedAction {
            (requireActivity() as MainActivity).finish()
        }

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