package com.example.busition_driver.main_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busition_driver.R
import com.example.busition_driver.databinding.FragmentScheduleListBinding
import com.example.busition_driver.main_screen.placeholder.PlaceholderContent


/**
 * A fragment representing a list of Items.
 */
class ScheduleList : Fragment() {

    private var columnCount = 1
    private var binding: FragmentScheduleListBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_list, container, false)
        // Set the adapter
        Log.d("asdsad", view.toString())
        if (view is RecyclerView) {
            binding = FragmentScheduleListBinding.inflate(inflater, container, false)
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS, binding)
            }

        }

        return view
    }


    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ScheduleList().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}


