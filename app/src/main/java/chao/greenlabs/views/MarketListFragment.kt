package chao.greenlabs.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.datamodels.MarketData
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.viewmodels.factories.MarketListVMFactory
import chao.greenlabs.viewmodels.MarketListViewModel
import chao.greenlabs.viewmodels.factories.ManageMarketVMFactory
import chao.greenlabs.views.adpaters.MarketAdapter
import kotlinx.android.synthetic.main.fragment_market_list.*

class MarketListFragment : Fragment() {

    private lateinit var listViewModel: MarketListViewModel
    private lateinit var manageMarketViewModel: ManageMarketViewModel
    private lateinit var adapter: MarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_market_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Repository(requireContext())
        getViewModel()
        setViews()
        registerObservers()
        setListeners()
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        listViewModel.clearMarketData()
    }

    private fun getViewModel() {
        val repository = Repository(requireContext())
        val factory = MarketListVMFactory(repository)
        listViewModel = ViewModelProvider(this, factory).get(MarketListViewModel::class.java)

        val manageMarketVMFactory = ManageMarketVMFactory(repository)
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
        adapter = MarketAdapter(onClickedListener)
        rv_markets.layoutManager = LinearLayoutManager(requireContext())
        rv_markets.setHasFixedSize(true)
        rv_markets.adapter = adapter
    }

    private fun registerObservers() {
        listViewModel.getMarketList().observe(viewLifecycleOwner, Observer { list ->
            adapter.setList(list)
            tv_market_count.text = getString(R.string.joined_market, list.size)
        })

        listViewModel.getTotalIncome().observe(viewLifecycleOwner, Observer { income ->
            tv_market_income.text = getString(R.string.total_market_income, income)
        })
    }

    private fun setListeners() {
        ll_back.setOnClickListener {
            findNavController().popBackStack()
        }

        ll_add.setOnClickListener {
            findNavController().navigate(R.id.action_marketListFragment_to_addMarketFragment)
        }
    }

    private fun loadData() {
        listViewModel.loadMarketData()
        manageMarketViewModel.loadItemData()
    }
}