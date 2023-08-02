package io.busition.driver.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.busition.driver.data.login.LoginDataSource
import io.busition.driver.data.login.LoginRepository

class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}