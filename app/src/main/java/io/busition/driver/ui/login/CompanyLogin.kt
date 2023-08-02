package io.busition.driver.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.busition.driver.R
import io.busition.driver.databinding.FragmentCompanyLoginBinding
import io.busition.driver.main_screen.MainScreen

class CompanyLogin : Fragment() {
    lateinit var binding: FragmentCompanyLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.companyButton.setOnClickListener {
            MainScreen.user_type = "company"
            Log.d("user type", MainScreen.user_type)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_company_login, container, false)
        binding = FragmentCompanyLoginBinding.bind(view)
        return view
    }


}