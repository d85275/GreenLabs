package chao.greenlabs.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.DialogUtils
import chao.greenlabs.utils.KeyboardUtils
import chao.greenlabs.viewmodels.AddCustomerViewModel
import chao.greenlabs.viewmodels.factories.AddCustomerVMFactory
import chao.greenlabs.views.adpaters.addcustomer.SoldItemAdapter
import kotlinx.android.synthetic.main.fragment_add_customer.*
import kotlinx.android.synthetic.main.fragment_add_customer.ll_back

class AddCustomerFragment : BaseFragment() {

    private lateinit var viewModel: AddCustomerViewModel

    private lateinit var soldAdapter: SoldItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewId(R.layout.fragment_add_customer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel()
        setViews()
        registerObservers()
        setListeners()
        KeyboardUtils.hideKeyboard(requireContext(), view)
    }

    private fun getViewModel() {
        val factory = AddCustomerVMFactory(Repository(requireContext()))
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(AddCustomerViewModel::class.java)
    }

    private fun setViews() {
        soldAdapter =
            SoldItemAdapter(viewModel)
        rv_sold_items.layoutManager = LinearLayoutManager(requireContext())
        rv_sold_items.setHasFixedSize(true)
        rv_sold_items.adapter = soldAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        setOnBackPressedAction {
            val list = viewModel.getCustomerData().value?.soldDataList
            if (list == null || list.size == 0) {
                findNavController().popBackStack()
            } else {
                showBackWarningDialog()
            }
        }

        et_search.isFocusable = false
        et_search.setOnClickListener {
            findNavController().navigate(R.id.action_addCustomerFragment_to_addCustomerSoldItemFragment)
            removeBackCallback()
        }

        ll_add.setOnClickListener {
            val list = viewModel.getCustomerData().value?.soldDataList
            if (list == null || list.size == 0) {
                findNavController().popBackStack()
            } else {
                viewModel.saveCustomer(et_memo.text.toString())
            }
            KeyboardUtils.hideKeyboard(requireContext(), view)
        }

        ll_back.setOnClickListener {
            val list = viewModel.getCustomerData().value?.soldDataList
            if (list == null || list.size == 0) {
                findNavController().popBackStack()
            } else {
                showBackWarningDialog()
            }
        }

        ll_discount.setOnClickListener {
            val default = getString(R.string.discount_title)
            val discount = if (tv_discount.text.toString() == default) {
                ""
            } else {
                tv_discount.text.toString().split(" ")[1]
            }
            val confirmAction: (discount: String) -> Unit = {
                if (it.isNotEmpty()) {
                    tv_discount.text = getString(R.string.price, it)
                }
                viewModel.updateDiscount(it)
                KeyboardUtils.hideKeyboard(requireContext(), view)
            }
            DialogUtils.showEditNumber(requireContext(), confirmAction, discount)
        }
    }

    private fun showBackWarningDialog() {
        val msg = getString(R.string.back_warning)
        DialogUtils.showQuestion(requireContext(), msg) { findNavController().popBackStack() }
    }

    private fun registerObservers() {
        viewModel.getCustomerData().observe(viewLifecycleOwner, Observer { customerData ->
            val list = customerData.soldDataList ?: return@Observer
            soldAdapter.setItem(list.map { it.copy() })
            val total = customerData.total
            val discount = customerData.discount
            val memo = customerData.memo

            tv_total.text = (total - discount).toString()
            tv_discount.text = getString(R.string.price, discount.toString())
            et_memo.setText(memo)
            et_memo.setSelection(memo.length)
        })

        viewModel.getIsCustomerSaved().observe(viewLifecycleOwner, Observer { isCustomerSaved ->
            if (isCustomerSaved) {
                findNavController().popBackStack()
                viewModel.setIsCustomerSaved(false)
            }
        })
    }
}