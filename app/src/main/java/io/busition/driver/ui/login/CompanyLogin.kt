package io.busition.driver.ui.login

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import io.busition.driver.R
import io.busition.driver.base_api_url
import io.busition.driver.data.RetrofitServiceInterface
import io.busition.driver.databinding.FragmentCompanyLoginBinding
import io.busition.driver.main_screen.MainScreen
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CompanyLogin : Fragment() {
    lateinit var binding: FragmentCompanyLoginBinding
    lateinit var username: String
    lateinit var password: String
    lateinit var loginButton: Button
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_company_login, container, false)
        binding = FragmentCompanyLoginBinding.bind(view)
        username = binding.companyId.text.toString()
        password = binding.companyPassword.text.toString()
        loginButton = binding.companyButton
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    binding.companyId.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    binding.companyPassword.error = getString(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    binding.companyId.text.toString(),
                    binding.companyPassword.text.toString(),
                    "company"
                )
            }
        }
        binding.companyId.addTextChangedListener(afterTextChangedListener)
        binding.companyPassword.addTextChangedListener(afterTextChangedListener)
        binding.companyPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    binding.companyId.text.toString(),
                    binding.companyPassword.text.toString(),
                    "Busition-Driver-Org"
                )
            }
            false
        }
        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        binding.companyButton.setOnClickListener {
            MainScreen.user_type = "company"
            loginViewModel.login(
                binding.companyId.text.toString(),
                binding.companyPassword.text.toString(),
                "Busition-Driver-Org"
            )
        }


    }
    private fun loginSuccess() {
        val intent = Intent(getActivity(), MainScreen::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val appContext = context?.applicationContext ?: return
        loginSuccess()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}