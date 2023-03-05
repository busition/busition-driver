package com.example.busition_driver.data

import com.example.busition_driver.data.model.LoggedInUser
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String , password: String): Result<LoggedInUser> {
        try {




            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe", 1)
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}