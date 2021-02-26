package chao.greenlabs.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.datamodels.ItemDetails
import chao.greenlabs.viewmodels.SoldItemDetailsViewModel
import chao.greenlabs.views.adpaters.ItemDetailsAdapter
import kotlinx.android.synthetic.main.fragment_item_details.*

class SoldItemDetailsFragment : BaseFragment() {

    private lateinit var viewModel: SoldItemDetailsViewModel
    private lateinit var adapter: ItemDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_item_details)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModels()
        registerObservers()
        setViews()
    }

    private fun getViewModels() {
        viewModel = ViewModelProvider(requireActivity()).get(SoldItemDetailsViewModel::class.java)
    }

    private fun registerObservers() {

    }

    private fun setViews() {
        tv_name.text = "Test"
        tv_price.text = "$ 100"

        adapter = ItemDetailsAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_details.setHasFixedSize(true)
        rv_details.layoutManager = layoutManager
        rv_details.adapter = adapter

        setTestData()
    }

    private fun setTestData() {
        Log.e("123", "set test data")
        val testDetails = arrayListOf<ItemDetails>()
        testDetails.add(ItemDetails("款式", arrayListOf("耳針", "耳夾")))
        testDetails.add(ItemDetails("寶石", arrayListOf("白水晶", "粉晶", "珍珠")))
        testDetails.add(ItemDetails("顏色", arrayListOf("白色", "粉色")))
        adapter.setList(testDetails)
    }
}