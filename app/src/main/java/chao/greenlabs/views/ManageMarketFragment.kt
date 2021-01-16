package chao.greenlabs.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.viewmodels.factories.MarketListVMFactory
import chao.greenlabs.viewmodels.MarketListViewModel
import chao.greenlabs.viewmodels.factories.ManageMarketVMFactory
import chao.greenlabs.views.adpaters.MarketAdapter
import chao.greenlabs.views.adpaters.SearchedItemAdapter
import chao.greenlabs.views.adpaters.SoldItemAdapter
import kotlinx.android.synthetic.main.fragment_manage_market.*
import kotlinx.android.synthetic.main.fragment_market_list.*

class ManageMarketFragment : Fragment() {

    private lateinit var viewModel: ManageMarketViewModel
    private lateinit var searchedAdapter: SearchedItemAdapter
    private lateinit var soldAdapter: SoldItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_market, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Repository(requireContext())
        getViewModel()
        setViews()
        registerObservers()
        setListeners()
    }

    private fun getViewModel() {
        val factory = ManageMarketVMFactory(
            Repository(requireContext())
        )
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(ManageMarketViewModel::class.java)
    }

    private fun setViews() {
        searchedAdapter = SearchedItemAdapter(viewModel)
        rv_searched_items.layoutManager = LinearLayoutManager(requireContext())
        rv_searched_items.setHasFixedSize(true)
        rv_searched_items.adapter = searchedAdapter

        soldAdapter = SoldItemAdapter(viewModel)
        rv_sold_items.layoutManager = LinearLayoutManager(requireContext())
        rv_sold_items.setHasFixedSize(true)
        rv_sold_items.adapter = soldAdapter
    }

    private fun registerObservers() {
        viewModel.getMarketName().observe(viewLifecycleOwner, Observer { name ->
            tv_market_name.text = name
        })

        viewModel.getMarketDate().observe(viewLifecycleOwner, Observer { date ->
            tv_market_date.text = date
        })

        viewModel.getMatchedItems().observe(viewLifecycleOwner, Observer { list ->
            list.forEach {
                Log.e("123", "name: ${it.name}, price: ${it.price}")
            }
            searchedAdapter.setList(list)
        })
    }

    private fun setListeners() {
        et_search.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val text = s.toString()
            if (text.isEmpty()) {
                rv_searched_items.visibility = View.GONE
            } else {
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