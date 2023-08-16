package io.busition.driver.main_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.busition.driver.roomDB.AppDatabase
import io.busition.driver.roomDB.Passenger
import io.busition.driver.roomDB.PassengerDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PassengerViewModel(context: Context):ViewModel() {
    private val _currentPassengerNum:MutableLiveData<String> = MutableLiveData("0 / 0")
    val currentPassengerNum:MutableLiveData<String>
        get() = _currentPassengerNum
    private val passengerDao: PassengerDao

    init {
        passengerDao = AppDatabase.getInstance(context).passengerDao()
    }

    suspend fun getPassengersByStationId(stationId: String): List<Passenger> {
        return withContext(Dispatchers.IO) {
            passengerDao.getPassengersByStationId(stationId)
        }
    }
    fun setCurrentPassengerNum(num:String){
        _currentPassengerNum.value = num
        Log.d("currentPassengerNum", num)
    }
}