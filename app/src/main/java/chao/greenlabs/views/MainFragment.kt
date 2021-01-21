package chao.greenlabs.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import chao.greenlabs.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        bt_manage_item.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_ItemListFragment)
        }
        bt_manage_market.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_MarketListFragment)
        }
    }
}