package chao.greenlabs.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.BottomSheetController
import chao.greenlabs.utils.DateTimeUtils
import chao.greenlabs.viewmodels.AddMarketSetDateViewModel
import chao.greenlabs.viewmodels.AddMarketViewModel
import chao.greenlabs.viewmodels.factories.AddMarketSetDateVMFactory
import chao.greenlabs.viewmodels.factories.AddMarketVMFactory
import chao.greenlabs.views.adpaters.AddMarketAdapter
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import kotlinx.android.synthetic.main.fragment_add_market_set_date.*
import java.util.*

class AddMarketSetDateFragment : Fragment() {

    private lateinit var viewModel: AddMarketViewModel
    private lateinit var setDateViewModel: AddMarketSetDateViewModel
    private lateinit var bottomSheetController: BottomSheetController<ViewSetMarketInfo>
    private lateinit var adapter: AddMarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewModel.resetData()
    }

    private fun getViewModels() {
        val repository = Repository(requireContext())
        val factory = AddMarketVMFactory(repository)
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(AddMarketViewModel::class.java)

        val name = viewModel.marketName
        val location = viewModel.marketLocation
        val setDateFactory = AddMarketSetDateVMFactory(name, location, repository)
        setDateViewModel =
            ViewModelProvider(this, setDateFactory).get(AddMarketSetDateViewModel::class.java)
    }

    private fun setListeners() {
        ll_back.setOnClickListener {
            findNavController().popBackStack()
        }

        ll_done.setOnClickListener {
            // todo: to check the input dates
            findNavController().popBackStack(R.id.marketListFragment, false)
        }

        ccv_market_calendar.setListener(getCalendarListener())
    }

    private fun registerObservers() {
        setDateViewModel.getMarketList().observe(viewLifecycleOwner, Observer { list ->
            adapter.setList(list)
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
        v_set_market_info.init(DateTimeUtils.getCurrentDate(), bottomSheetController, setDateViewModel)

        adapter = AddMarketAdapter()
        rv_market_date.layoutManager = LinearLayoutManager(requireContext())
        rv_market_date.setHasFixedSize(true)
        rv_market_date.adapter = adapter
    }

    private fun getCalendarListener(): CompactCalendarView.CompactCalendarViewListener {
        return object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                if (dateClicked == null) return
                //selectedDay.value = dateClicked
                Log.e("123", "on day clicked: ${DateTimeUtils.getDateString(dateClicked)}")
                v_set_market_info.setDate(DateTimeUtils.getDateString(dateClicked))
                bottomSheetController.show()
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                if (firstDayOfNewMonth == null) return
                Log.e("123", "on Month Scroll")
                tv_calendar_month.text = DateTimeUtils.getMonthString(firstDayOfNewMonth)
            }
        }
    }
}