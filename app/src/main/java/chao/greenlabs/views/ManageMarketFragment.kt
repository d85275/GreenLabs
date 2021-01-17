package chao.greenlabs.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemData
import chao.greenlabs.repository.Repository
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.viewmodels.factories.ManageMarketVMFactory
import chao.greenlabs.views.adpaters.SearchedItemAdapter
import chao.greenlabs.views.adpaters.SoldItemAdapter
import kotlinx.android.synthetic.main.fragment_manage_market.*

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
        val onClickedListener: ((itemData: ItemData) -> Unit) = { item ->
            et_search.text.clear()
            soldAdapter.addItem(item)
        }
        searchedAdapter = SearchedItemAdapter(viewModel, onClickedListener)
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
                rv_sold_items.visibility = View.VISIBLE
                rv_searched_items.visibility = View.GONE
            } else {
                rv_sold_items.visibility = View.GONE
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