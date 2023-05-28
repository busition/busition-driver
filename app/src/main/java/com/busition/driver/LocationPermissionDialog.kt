package com.busition.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.busition.driver.databinding.LocationPermissionDialogBinding

class LocationPermissionDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = false
    }

    private lateinit var binding: LocationPermissionDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LocationPermissionDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = "Busition Driver 앱은 앱이 종료되었거나 사용 중이 아닐 때도 위치 데이터를 수집하여 버스 실시간 위치 정보 제공 및 승하차 자동 기능을 제공합니다.\n" +

                "위치 정보 수집에 동의하지 않으시면 Busition Driver 앱을 사용하실 수 없습니다.\n" + "위치 정보 수집에 동의하시겠습니까?"


        binding.tvSample.text = text

        binding.denyBtn.setOnClickListener {

        }
        binding.accessBtn.setOnClickListener {
            MainActivity.type = 1
        }
    }
}