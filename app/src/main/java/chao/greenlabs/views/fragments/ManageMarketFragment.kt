package chao.greenlabs.views.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.datamodels.CustomerData
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.AnimUtils
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.utils.ToastUtils
import chao.greenlabs.viewmodels.AddCustomerViewModel
import chao.greenlabs.viewmodels.ManageMarketViewModel
import chao.greenlabs.viewmodels.factories.AddCustomerVMFactory
import chao.greenlabs.viewmodels.factories.ManageMarketVMFactory
import chao.greenlabs.views.adpaters.CustomerAdapter
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_manage_market.*
import kotlin.math.abs

private const val COLLAPSE_TOTAL_VIEW_OFFSET = -30f

class ManageMarketFragment : BaseFragment() {

    private lateinit var viewModel: ManageMarketViewModel
    private lateinit var addCustomerViewModel: AddCustomerViewModel
    private lateinit var customerAdapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_manage_market)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        getViewModel()
        setViews()
        registerObservers()
        setListeners()
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearMarketSoldData()
        addCustomerViewModel.clearData()
    }

    private fun getViewModel() {
        val factory = ManageMarketVMFactory(resources, Repository(requireContext()))
        val addCustomerFactory = AddCustomerVMFactory(Repository(requireContext()))
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(ManageMarketViewModel::class.java)

        addCustomerViewModel =
            ViewModelProvider(
                requireActivity(),
                addCustomerFactory
            ).get(AddCustomerViewModel::class.java)
    }

    private fun setViews() {
        customerAdapter = CustomerAdapter(viewModel) { customerData ->
            findNavController().navigate(R.id.action_manageMarketFragment_to_addCustomerFragment)
            addCustomerViewModel.setCustomer(customerData)
            addCustomerViewModel.isUpdateMode = customerData.soldDataList!!.isNotEmpty()
        }
        rv_customers.layoutManager = LinearLayoutManager(requireContext())
        rv_customers.setHasFixedSize(true)
        rv_customers.adapter = customerAdapter

        v_market_detail.init(cl_parent, viewModel)
    }

    private fun registerObservers() {
        viewModel.getMarketData().observe(viewLifecycleOwner, Observer { data ->
            tv_title.text = data.name
            tv_market_date.text = data.date
            tv_market_income.text = data.income
            tv_collapsed_total.text = getString(R.string.price, data.income)
            v_market_detail.setMarketData(data)
        })

        viewModel.getCustomerList().observe(viewLifecycleOwner, Observer { customerList ->
            setList(customerList)
            if (customerList.isEmpty()) return@Observer
            //val y = rv_customers.getChildAt(customerList.lastIndex).y.toInt()
            //nest_scroll_view.scrollTo(0, y)
        })
    }

    private fun setList(customerList: List<CustomerData>) {
        customerAdapter.setCustomerList(customerList)
        dismissLoading()
    }

    private fun setListeners() {
        ll_copy.setOnClickListener {
            val copyData = viewModel.getCopyData()
            val myClipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val myClip: ClipData = ClipData.newPlainText("note_copy", copyData)
            myClipboard.setPrimaryClip(myClip)
            ToastUtils.show(requireContext(), getString(R.string.data_copied))
        }

        fab_add.setOnClickListener {
            val marketId = viewModel.getMarketData().value?.id!!
            findNavController().navigate(R.id.action_manageMarketFragment_to_addCustomerFragment)
            addCustomerViewModel.setCustomer(CustomerData.createNewCustomer(marketId))
            addCustomerViewModel.isUpdateMode = false
            viewModel.addBtnClicked()
        }

        ll_back.setOnClickListener {
            findNavController().popBackStack()
        }

        ll_detail.setOnClickListener {
            v_market_detail.showMarketDetail()
        }

        tv_title.setOnClickListener {
            val name = tv_title.text.toString()
            DialogUtils.showEditText(requireContext(), {
                viewModel.updateMarketName(it)
            }, name)
        }

        tv_market_date.setOnClickListener {
            val date = tv_market_date.text.toString()
            DialogUtils.showDatePicker(requireContext(), date) {
                viewModel.updateMarketDate(it)
            }
        }
        appbar.addOnOffsetChangedListener(onOffsetChangedListener)
    }

    private val onOffsetChangedListener =
        AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val fraction = abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            if (fraction == 0f) {
                ll_detail.visibility = View.VISIBLE
                AnimUtils.hideToRight(cl_parent, ll_collapsed_total)
            } else {
                if (ll_detail.visibility == View.VISIBLE) {
                    ll_detail.visibility = View.INVISIBLE
                }
                if (fraction == 1f) {
                    AnimUtils.showFromRight(
                        cl_parent,
                        ll_collapsed_total,
                        COLLAPSE_TOTAL_VIEW_OFFSET
                    )
                }
            }
            cl_top.alpha = 1 - fraction
        }

    private fun loadData() {
        viewModel.loadCustomers()
        addCustomerViewModel.loadItemData()
    }
}