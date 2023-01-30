package com.bjit.broadcastrecieverexample

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bjit.broadcastrecieverexample.databinding.ActivityMainBinding
import com.bjit.broadcastrecieverexample.services.AirplaneModeChangeReceiver
import com.bjit.broadcastrecieverexample.services.NetworkCheckService
import com.bjit.broadcastrecieverexample.services.PhoneNumberReadService
import com.bjit.broadcastrecieverexample.utils.Constants.Companion.preferenceId

class MainActivity : AppCompatActivity() {
    private val readPhoneStatePermissionCode = 1
    private val readCallLogPermissionCode = 2
    private val internetPermissionPackage = "android.net.conn.CONNECTIVITY_CHANGE"
    private val phonePermission = "android.intent.action.PHONE_STATE"
    private lateinit var receiver: AirplaneModeChangeReceiver
    private lateinit var internetPermission: NetworkCheckService
    private lateinit var phoneCallRead: PhoneNumberReadService
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences: SharedPreferences = getSharedPreferences(preferenceId, MODE_PRIVATE)
        val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
        requestPermission()
        receiver = AirplaneModeChangeReceiver()
        internetPermission = NetworkCheckService()
        phoneCallRead = PhoneNumberReadService()


        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(receiver, it)
        }
        IntentFilter(internetPermissionPackage).also {
            registerReceiver(internetPermission, it)
        }

        IntentFilter(phonePermission).also {
            registerReceiver(phoneCallRead, it)
        }

    }

    private fun requestPermission() {
        val phonePermission = Manifest.permission.READ_PHONE_STATE
        val callLogPermission = Manifest.permission.READ_CALL_LOG
        val permission = Manifest.permission.RECEIVE_SMS
        val grant = ContextCompat.checkSelfPermission(this, permission)
        val grantPhone = ContextCompat.checkSelfPermission(this, phonePermission)
        val grantCallLog = ContextCompat.checkSelfPermission(this, callLogPermission)
        if (grantPhone != PackageManager.PERMISSION_GRANTED) {
            getPermission(phonePermission, readPhoneStatePermissionCode)
        }
        if (grantCallLog != PackageManager.PERMISSION_GRANTED) {
            getPermission(callLogPermission, readCallLogPermissionCode)
        }
        if (grant != PackageManager.PERMISSION_GRANTED) {
            getPermission(permission, 100)
        }
    }

    private fun getPermission(permission: String, permissionCode: Int) {
        ActivityCompat.requestPermissions(
            this, arrayOf(permission), permissionCode
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
        unregisterReceiver(internetPermission)
        unregisterReceiver(phoneCallRead)
    }
}