package com.busition.driver.ui.login

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.busition.driver.R
import com.busition.driver.data.login.LoginRepository
import com.busition.driver.data.login.Result
import com.busition.driver.main_screen.MainScreen
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String):String {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)
        var token:String = ""
        Log.d(TAG, "signIn:$username")
        //ㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴ
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Log.d("login user ", user.toString())
                    if (user != null) {
                        user.getIdToken(true)
                            .addOnCompleteListener(OnCompleteListener<GetTokenResult> { t ->
                                if (t.isSuccessful) {
                                    val idToken = t.result.token
                                    token = "Bearer "+idToken.toString()
                                    Log.d("id token" , idToken.toString())


                                } else {
                                    // Handle error -> task.getException();
                                }
                            })


//                        user.updatePhoneNumber()
                    }
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    //updateUI(null) 로그인 완료시
                    //checkForMultiFactorFailure(task.exception!!)
                }

                if (!task.isSuccessful) {
//                    binding.status.setText(R.string.auth_failed)
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }

            }

        //ㄴㄴㄴㄴㄴㄴㄴㄴ
        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName, driver_id = result.data.driver_id))

        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
        return token
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
}