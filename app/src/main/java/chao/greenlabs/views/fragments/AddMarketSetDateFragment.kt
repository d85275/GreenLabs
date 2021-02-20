package chao.greenlabs.views.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.*
import chao.greenlabs.viewmodels.AddMarketViewModel
import chao.greenlabs.viewmodels.factories.AddMarketSetDateVMFactory
import chao.greenlabs.views.ViewSetMarketInfo
import chao.greenlabs.views.adpaters.AddMarketAdapter
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_add_market_set_date.*
import kotlinx.android.synthetic.main.fragment_add_market_set_date.tv_location
import kotlinx.android.synthetic.main.fragment_add_market_set_date.tv_title
import kotlinx.android.synthetic.main.view_set_market_info.*
import java.util.*

private const val VIEW_DISABLE = 0.5f
private const val VIEW_ENABLE = 1.0f

class AddMarketSetDateFragment : Fragment() {

    private lateinit var viewModel: AddMarketViewModel
    private lateinit var bottomSheetController: BottomSheetController<ViewSetMarketInfo>
    private lateinit var adapter: AddMarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            backPressedCallback
        )
        return inflater.inflate(R.layout.fragment_add_market_set_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModels()
        initViews()
        setListeners()
        registerObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressedCallback.remove()
    }

    private fun getViewModels() {
        val repository = Repository(requireContext())
        val setDateFactory = AddMarketSetDateVMFactory(repository)
        viewModel =
            ViewModelProvider(requireActivity(), setDateFactory).get(AddMarketViewModel::class.java)
    }

    private fun setListeners() {
        ll_back.setOnClickListener {
            if (bottomSheetController.isShown()) return@setOnClickListener

            showBackWarningDialog()
        }

        ll_done.setOnClickListener {
            if (bottomSheetController.isShown()) return@setOnClickListener

            if (viewModel.getMarketListSize() <= 0) {
                val msg = getString(R.string.no_market_warning)
                DialogUtils.showQuestion(
                    requireContext(),
                    msg
                ) { findNavController().popBackStack(R.id.marketListFragment, false) }
            } else {
                viewModel.addMarketList()
            }
        }

        ccv_market_calendar.setListener(getCalendarListener())
    }

    private fun registerObservers() {
        viewModel.getAddDone().observe(viewLifecycleOwner, Observer { isAddDone ->
            if (isAddDone) {
                viewModel.clear()
                findNavController().popBackStack(R.id.marketListFragment, false)
            }
        })

        viewModel.getMarketList().observe(viewLifecycleOwner, Observer { list ->
            adapter.setList(list)
        })

        viewModel.getNewMarketData().observe(viewLifecycleOwner, Observer { marketData ->
            val date = DateTimeUtils.getCurrentDate(marketData.date) ?: return@Observer
            val event = Event(requireContext().getColor(R.color.colorPrimary), date.time)
            ccv_market_calendar.addEvents(listOf(event))
        })

        bottomSheetController.getShownState().observe(viewLifecycleOwner, Observer { isShown ->
            ll_back.alpha = if (isShown) VIEW_DISABLE else VIEW_ENABLE
            ll_done.alpha = if (isShown) VIEW_DISABLE else VIEW_ENABLE
        })
    }

    private fun initViews() {
        tv_title.text = viewModel.marketName
        tv_location.text = viewModel.marketLocation
        tv_calendar_month.text =
            DateTimeUtils.getMonthString(ccv_market_calendar.firstDayOfCurrentMonth)
        ccv_market_calendar.displayOtherMonthDays(true)
        ccv_market_calendar.shouldSelectFirstDayOfMonthOnScroll(false)
        ccv_market_calendar.shouldDrawIndicatorsBelowSelectedDays(true)

        bottomSheetController = BottomSheetController(v_set_market_info)
        v_set_market_info.init(
            DateTimeUtils.getCurrentDate(),
            bottomSheetController,
            viewModel
        )

        val itemTouchHelper =
            ItemTouchHelper(TouchCallbackUtils.getItemTouchHelperCallback { position ->
                onItemSwipedAction.invoke(position)
            })
        itemTouchHelper.attachToRecyclerView(rv_market_date)

        adapter = AddMarketAdapter()
        rv_market_date.layoutManager = LinearLayoutManager(requireContext())
        rv_market_date.setHasFixedSize(true)
        rv_market_date.adapter = adapter
    }

    private fun getCalendarListener(): CompactCalendarView.CompactCalendarViewListener {
        return object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                if (dateClicked == null) return
                val event = ccv_market_calendar.getEvents(dateClicked)
                val date = DateTimeUtils.getDateString(dateClicked)
                if (event.isNotEmpty()) {
                    // update
                    v_set_market_info.setConfirmButtonText(getString(R.string.update))
                    v_set_market_info.setFee(viewModel.getMarketFee(date))
                } else {
                    v_set_market_info.setConfirmButtonText(getString(R.string.confirm))
                }
                v_set_market_info.setDate(date)
                v_set_market_info.requireFocus()
                KeyboardUtils.showKeyboard(requireContext(), et_fee)
                Handler(requireContext().mainLooper).postDelayed(
                    { bottomSheetController.show() },
                    50
                )
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                if (firstDayOfNewMonth == null) return
                tv_calendar_month.text = DateTimeUtils.getMonthString(firstDayOfNewMonth)
            }
        }
    }

    private fun showBackWarningDialog() {
        val msg = getString(R.string.back_warning)
        DialogUtils.showQuestion(requireContext(), msg) { findNavController().popBackStack() }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (v_set_market_info.isViewShown()) {
                v_set_market_info.dismiss()
            } else {
                showBackWarningDialog()
            }
        }
    }

    private val onItemSwipedAction: ((position: Int) -> Unit) = { position ->
        val marketDate = viewModel.getMarketData(position)
        val date = DateTimeUtils.getCurrentDate(marketDate.date)!!
        val event = Event(requireContext().getColor(R.color.colorPrimary), date.time)
        ccv_market_calendar.removeEvent(event)
        viewModel.deleteMarket(position)
    }
}