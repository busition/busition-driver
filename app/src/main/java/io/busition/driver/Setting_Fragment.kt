package io.busition.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

class Setting_Fragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.hide()
        return inflater.inflate(R.layout.fragment_setting_, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.show()
    }

}