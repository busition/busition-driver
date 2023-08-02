package com.busition.driver.ui.login

import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.Navigation
import com.busition.driver.databinding.FragmentRegistBinding

import com.busition.driver.R
import com.busition.driver.base_api_url
import com.busition.driver.data.RetrofitServiceInterface
import com.busition.driver.data.signup.SignUpBody
import com.busition.driver.data.signup.SignUpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentRegistBinding? = null

    private val binding get() = _binding!!
    private lateinit var registbutton : Button
    var authType = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegistBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val arg = requireArguments()
        authType = arg.getString("auth_type").toString()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)


        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val nameEditText = binding.editname
        val phoneEditText = binding.editTextPhone
        registbutton = binding.regist
        registbutton.isEnabled = false
        val loadingProgressBar = binding.loading

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

        registbutton.setOnClickListener {
            Log.d("regist ", "create Account : ${usernameEditText.text}")
            val retrofit = Retrofit.Builder()
                .baseUrl(base_api_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val data = SignUpBody(
                nameEditText.text.toString(),
                usernameEditText.text.toString(),
                passwordEditText.text.toString(),
            )

            val service = retrofit.create(RetrofitServiceInterface::class.java)
            service.signUp(data).enqueue(object : retrofit2.Callback<SignUpResponse> {
                override fun onResponse(
                    call: Call<SignUpResponse>,
                    response: Response<SignUpResponse>
                ) {
                    if (response.isSuccessful){
                        val navController = Navigation.findNavController(view!!)
                        navController.navigate(R.id.action_BusitionRegister_to_BusitionLogin)
                        Toast.makeText(requireContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
                    }else if(response.code() == 400){
                        Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
                    }else{
                        Log.d("regist fail ", "create Account : $response")
                        Toast.makeText(requireContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    Log.d("regist ", "create Account : ${t.message}")
                    Toast.makeText(requireContext(), "네트워크 오류 발생!", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    fun registDataChanged(username: String, password: String,name:String,phone:String) {
        registbutton.isEnabled = username!="" && phone != "" && password != "" && name != ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}