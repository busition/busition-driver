package io.busition.driver.main_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PassengerViewModel:ViewModel() {
    private val _currentPassengerNum:MutableLiveData<String> = MutableLiveData("0 / 0")
    val currentPassengerNum:MutableLiveData<String>
        get() = _currentPassengerNum
    fun setCurrentPassengerNum(num:String){
        _currentPassengerNum.value = num
        Log.d("currentPassengerNum", num)
    }
}