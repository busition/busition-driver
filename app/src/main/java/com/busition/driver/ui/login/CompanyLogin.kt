package com.busition.driver.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.busition.driver.R
import com.busition.driver.databinding.FragmentCompanyLoginBinding
import com.busition.driver.main_screen.MainScreen


class CompanyLogin : Fragment() {
    val binding: FragmentCompanyLoginBinding = FragmentCompanyLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.companyButton.setOnClickListener {
            MainScreen.user_type = "company"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_company_login, container, false)
    }


}