package chao.greenlabs.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import chao.greenlabs.R
import chao.greenlabs.repository.Repository
import chao.greenlabs.utils.BottomSheetController
import chao.greenlabs.utils.DateUtils
import chao.greenlabs.viewmodels.AddMarketViewModel
import chao.greenlabs.viewmodels.factories.AddMarketVMFactory
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_add_market_set_date.*
import java.util.*

class AddMarketSetDateFragment : Fragment() {

    private lateinit var viewModel: AddMarketViewModel
    private lateinit var bottomSheetController: BottomSheetController<ViewSetMarketInfo>

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
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetData()
    }

    private fun getViewModels() {
        val factory = AddMarketVMFactory(
            Repository(requireContext())
        )
        viewModel =
            ViewModelProvider(requireActivity(), factory).get(AddMarketViewModel::class.java)
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

    private fun initViews() {
        tv_title.text = viewModel.marketName
        tv_location.text = viewModel.marketLocation
        tv_calendar_month.text =
            DateUtils.getMonthString(ccv_market_calendar.firstDayOfCurrentMonth)
        ccv_market_calendar.displayOtherMonthDays(true)
        ccv_market_calendar.shouldSelectFirstDayOfMonthOnScroll(false)
        ccv_market_calendar.shouldDrawIndicatorsBelowSelectedDays(true)

        bottomSheetController = BottomSheetController(v_set_market_info)
        v_set_market_info.setBottomSheetController(bottomSheetController)
    }

    private fun getCalendarListener(): CompactCalendarView.CompactCalendarViewListener {
        return object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                if (dateClicked == null) return
                //selectedDay.value = dateClicked
                Log.e("123", "on day clicked: ${DateUtils.getDateString(dateClicked)}")
                v_set_market_info.setDate(DateUtils.getDateString(dateClicked))
                bottomSheetController.show()
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                if (firstDayOfNewMonth == null) return
                Log.e("123", "on Month Scroll")
                tv_calendar_month.text = DateUtils.getMonthString(firstDayOfNewMonth)
            }
        }
    }
}