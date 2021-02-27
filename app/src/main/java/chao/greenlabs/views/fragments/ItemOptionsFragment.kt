package chao.greenlabs.views.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.viewmodels.ItemOptionsViewModel
import chao.greenlabs.views.adpaters.ItemOptionsAdapter
import kotlinx.android.synthetic.main.fragment_item_options.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ItemOptionsFragment : BaseFragment() {

    private lateinit var viewModel: ItemOptionsViewModel
    private lateinit var adapter: ItemOptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_item_options)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModels()
        setViews()
        setListeners()
        registerObservers()
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }

    private fun getViewModels() {
        viewModel = ViewModelProvider(requireActivity()).get(ItemOptionsViewModel::class.java)
    }

    private fun registerObservers() {
        //viewModel.getOptionsData().observe(viewLifecycleOwner, Observer { options ->
        //    adapter.setList(options)
        //})

        viewModel.getIsOptionsSelected().observe(viewLifecycleOwner, Observer { isOptionsSelected ->
            if (isOptionsSelected) {
                tv_add_item.alpha = 1f
                tv_add_item.setOnClickListener {
                    viewModel.save()
                }
            } else {
                tv_add_item.alpha = 0.5f
                tv_add_item.setOnClickListener(null)
            }
        })
    }

    private fun loadData() {
        runBlocking(Dispatchers.IO) {
            val options = viewModel.loadData()
            adapter.setList(options)
        }
    }

    private fun setViews() {
        tv_name.text = "Test"
        tv_price.text = "$ 100"

        adapter = ItemOptionsAdapter(viewModel)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_details.setHasFixedSize(true)
        rv_details.layoutManager = layoutManager
        rv_details.adapter = adapter
    }

    private fun setListeners() {
        ll_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}