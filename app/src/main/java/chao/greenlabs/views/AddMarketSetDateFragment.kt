package chao.greenlabs.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import chao.greenlabs.R
import chao.greenlabs.utils.DateUtils
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import kotlinx.android.synthetic.main.fragment_add_market_set_date.*
import java.util.*

class AddMarketSetDateFragment : Fragment() {

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

    private fun getViewModels() {

    }

    private fun setListeners() {
        ll_done.setOnClickListener {
            // todo: to check the input dates
            findNavController().popBackStack(R.id.marketListFragment, false)
        }
        
        ccv_market_calendar.setListener(getCalendarListener())
    }

    private fun initViews() {
        tv_calendar_month.text =
            DateUtils.getMonthString(ccv_market_calendar.firstDayOfCurrentMonth)
        ccv_market_calendar.displayOtherMonthDays(true)
        ccv_market_calendar.shouldSelectFirstDayOfMonthOnScroll(false)
        ccv_market_calendar.shouldDrawIndicatorsBelowSelectedDays(true)
    }

    private fun getCalendarListener(): CompactCalendarView.CompactCalendarViewListener {
        return object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
                if (dateClicked == null) return
                //selectedDay.value = dateClicked
                Log.e("123", "on day clicked: ${DateUtils.getDateString(dateClicked)}")
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                if (firstDayOfNewMonth == null) return
                Log.e("123", "on Month Scroll")
                tv_calendar_month.text = DateUtils.getMonthString(firstDayOfNewMonth)
            }
        }
    }
}