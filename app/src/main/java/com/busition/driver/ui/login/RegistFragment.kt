package com.busition.driver.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
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
import androidx.navigation.Navigation
import com.busition.driver.databinding.FragmentRegistBinding

import com.busition.driver.R
import com.busition.driver.base_api_url
import com.busition.driver.data.RetrofitServiceInterface
import com.busition.driver.data.signup.SignUpBody
import com.busition.driver.data.signup.SignUpResponse
import com.busition.driver.main_screen.MainScreen
import com.busition.driver.main_screen.MainScreen.Companion.token
import com.busition.driver.ui.login.LoginViewModel.Companion.auth
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.GetTokenResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentRegistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var loginButton : Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val nameEditText = binding.editname
        val phoneEditText = binding.editTextPhone
        loginButton = binding.login
        loginButton.isEnabled = false
        val loadingProgressBar = binding.loading

//        loginViewModel.loginFormState.observe(viewLifecycleOwner,
//            Observer { loginFormState ->
//                if (loginFormState == null) {
//                    return@Observer
//                }
//                loginButton.isEnabled = loginFormState.isDataValid
//                loginFormState.usernameError?.let {
//                    usernameEditText.error = getString(it)
//                }
//                loginFormState.passwordError?.let {
//                    passwordEditText.error = getString(it)
//                }
//            })
//
//        loginViewModel.loginResult.observe(viewLifecycleOwner,
//            Observer { loginResult ->
//                loginResult ?: return@Observer
//                loadingProgressBar.visibility = View.GONE
//                loginResult.error?.let {
//                    showLoginFailed(it)
//                }
//                loginResult.success?.let {
//                    updateUiWithUser(it)
//                }
//            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                registDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString(),
                    nameEditText.text.toString(),
                    phoneEditText.text.toString()

                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        nameEditText.addTextChangedListener(afterTextChangedListener)
        phoneEditText.addTextChangedListener(afterTextChangedListener)


        loginButton.setOnClickListener {
//            loadingProgressBar.visibility = View.VISIBLE
            //regist
            createAccount(nameEditText.text.toString(),usernameEditText.text.toString(), passwordEditText.text.toString(), phoneEditText.text.toString())


        }
    }

    private fun createAccount(name: String,email: String, password: String, phone: String) {
        Log.d("fire base", "createAccount:$email")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("fire base", "createUserWithEmail:success")
                    val user = auth.currentUser
                    val data = SignUpBody(
                        1,
                        email,
                        name,
                        phone
                    )
                    val retrofit = Retrofit.Builder()
                        .baseUrl(base_api_url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val service = retrofit.create(RetrofitServiceInterface::class.java)
                    user?.getIdToken(true)
                        ?.addOnCompleteListener(OnCompleteListener<GetTokenResult> { t ->
                            if (t.isSuccessful) {
                                val idToken = t.result.token
                                MainScreen.token = "Bearer " + idToken.toString()
                                Log.d("id token", idToken.toString())


                            } else {
                                // Handle error -> task.getException();
                            }
                        })
                    service.signUp(token,data).enqueue(object : retrofit2.Callback<SignUpResponse> {
                        override fun onResponse(
                            call: Call<SignUpResponse>,
                            response: Response<SignUpResponse>
                        ) {
                            val navController = Navigation.findNavController(view!!)
                            navController.navigate(R.id.action_BusitionRegister_to_BusitionLogin)
                        }

                        override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("fire base", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

            }
    }
    fun registDataChanged(username: String, password: String,name:String,phone:String) {
        if (username=="" || phone == "") {

        } else if (password =="" || name == "" ) {

        } else {
            loginButton.isEnabled = true
        }
    }
    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.driver_uuid
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}