package com.busition.driver.ui.login

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.busition.driver.R
import com.busition.driver.base_api_url
import com.busition.driver.data.RetrofitServiceInterface
import com.busition.driver.data.login.LoginRepository
import com.busition.driver.data.login.Result
import com.busition.driver.data.signin.SignIn
import com.busition.driver.main_screen.MainScreen
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {


    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult


    fun login(username: String, password: String) {
        Log.d("login", "username: $username, password: $password")
        if (username == "" || password == "") {
            _loginResult.value = LoginResult(error = R.string.login_failed)
            return
        }
        var token = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                _loginResult.value = LoginResult(error = R.string.login_failed)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result

            // Log and toast

        })
        Log.d("fcm device token : => ", token)
        val retrofit = Retrofit.Builder()
            .baseUrl(base_api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitServiceInterface::class.java)
        service.sighIn(username,password,token).enqueue(object : retrofit2.Callback<com.busition.driver.data.signin.SigninResponse> {
            override fun onResponse(
                call: retrofit2.Call<com.busition.driver.data.signin.SigninResponse>,
                response: retrofit2.Response<com.busition.driver.data.signin.SigninResponse>
            ) {
                if (response.isSuccessful) {
                    val token = "Bearer"+ " " + response.body()!!.access_token
                    Log.d("token", token.toString())
                    auth_token = token
                    MainScreen.token = token
                    _loginResult.value =
                        LoginResult(success = LoggedInUserView(Authorization = token.toString()))

                } else {
                    Log.d("login failed", "login failed $response")
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            }

            override fun onFailure(
                call: retrofit2.Call<com.busition.driver.data.signin.SigninResponse>,
                t: Throwable
            ) {
                Log.d("login failed", "login failed $t")
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
        )
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    companion object {
        var auth: FirebaseAuth = Firebase.auth
        var auth_token :String = ""

    }
}