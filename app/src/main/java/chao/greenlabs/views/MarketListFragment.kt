package chao.greenlabs.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.factories.MarketListVMFactory
import chao.greenlabs.viewmodels.MarketListViewModel
import chao.greenlabs.views.adpaters.MarketAdapter
import kotlinx.android.synthetic.main.fragment_market_list.*

class MarketListFragment : Fragment() {

    private lateinit var listViewModel: MarketListViewModel
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
        listViewModel.loadMarketData()
    }

    private fun getViewModel() {
        val factory = MarketListVMFactory(
            Repository(requireContext())
        )
        listViewModel = ViewModelProvider(this, factory).get(MarketListViewModel::class.java)
    }

    private fun setViews() {
        adapter = MarketAdapter()
        rv_markets.layoutManager = LinearLayoutManager(requireContext())
        rv_markets.setHasFixedSize(true)
        rv_markets.adapter = adapter
    }

    private fun registerObservers() {
        listViewModel.getMarketList().observe(viewLifecycleOwner, Observer { list ->
            adapter.setList(list)
        })
    }
}