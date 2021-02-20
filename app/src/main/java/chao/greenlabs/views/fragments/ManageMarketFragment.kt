package chao.greenlabs.views.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import kotlinx.android.synthetic.main.fragment_manage_market.*
import kotlinx.android.synthetic.main.fragment_manage_market.ll_back
import kotlinx.android.synthetic.main.fragment_manage_market.tv_market_income
import kotlinx.android.synthetic.main.fragment_manage_market.tv_title

class ManageMarketFragment : BaseFragment() {

    private lateinit var viewModel: ManageMarketViewModel
    private lateinit var addCustomerViewModel: AddCustomerViewModel
    private lateinit var customerAdapter: CustomerAdapter
    private var isMarketDetailShown = false

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

        AnimUtils.initManageMarketDetailState(cl_parent, cl_market_detail)
    }

    private fun registerObservers() {
        viewModel.getMarketData().observe(viewLifecycleOwner, Observer { data ->
            tv_title.text = data.name
            tv_start_time.text = data.startTime
            tv_end_time.text = data.endTime
            tv_market_date.text = data.date
            tv_market_fee.text = data.fee
            tv_market_income.text = data.income
            tv_market_location.text = data.location
        })

        viewModel.getCustomerList().observe(viewLifecycleOwner, Observer { customerList ->
            dismissLoading()
            customerAdapter.setCustomerList(customerList)
            if (viewModel.shouldScrollToBottom(customerList.size)) {
                rv_customers.scrollToPosition(customerList.lastIndex)
            }
        })
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
            showMarketDetailAction.invoke()
        }

        tv_title.setOnClickListener {
            val name = tv_title.text.toString()
            DialogUtils.showEditText(requireContext(), {
                viewModel.updateMarketName(it)
            }, name)
        }

        tv_market_location.setOnClickListener {
            val location = tv_market_location.text.toString()
            DialogUtils.showEditText(requireContext(), {
                viewModel.updateMarketLocation(it)
            }, location)
        }

        tv_market_fee.setOnClickListener {
            val price = tv_market_fee.text.toString()
            DialogUtils.showEditNumber(requireContext(), {
                viewModel.updateMarketFee(it)
            }, price)
        }

        tv_start_time.setOnClickListener {
            val hour = tv_start_time.text.toString().split(":")[0].trim()
            val min = tv_start_time.text.toString().split(":")[1].trim()
            DialogUtils.showTimePicker(requireContext(), hour, min) { h, m ->
                viewModel.updateMarketStartTime(h, m)
            }
        }

        tv_end_time.setOnClickListener {
            val hour = tv_end_time.text.toString().split(":")[0].trim()
            val min = tv_end_time.text.toString().split(":")[1].trim()
            DialogUtils.showTimePicker(requireContext(), hour, min) { h, m ->
                viewModel.updateMarketEndTime(h, m)
            }
        }

        tv_market_date.setOnClickListener {
            val date = tv_market_date.text.toString()
            DialogUtils.showDatePicker(requireContext(), date) {
                viewModel.updateMarketDate(it)
            }
        }
    }

    private fun loadData() {
        viewModel.loadCustomers()
        addCustomerViewModel.loadItemData()
    }

    private val showMarketDetailAction = {
        isMarketDetailShown = if (isMarketDetailShown) {
            AnimUtils.hideManageMarketDetail(cl_parent, cl_market_detail)
            false
        } else {
            AnimUtils.showManageMarketDetail(cl_parent, cl_market_detail)
            true
        }
    }


}