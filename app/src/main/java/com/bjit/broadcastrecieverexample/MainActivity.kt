package com.bjit.broadcastrecieverexample

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.TextView
import com.bjit.broadcastrecieverexample.databinding.ActivityMainBinding
import com.bjit.broadcastrecieverexample.services.AirplaneModeChangeReceiver
import com.bjit.broadcastrecieverexample.services.NetworkCheckService

class MainActivity : AppCompatActivity() {
    private val readPhoneStatePermissionCode = 1
    private val readCallLogPermissionCode = 2
    private val preferenceName = "name"
    private val preferenceId = "localSharedPref"
    private val internetPermissionPackage = "android.net.conn.CONNECTIVITY_CHANGE"
    private lateinit var receiver: AirplaneModeChangeReceiver
    private lateinit var internetPermission: NetworkCheckService
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

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(receiver, it)
        }
        IntentFilter(internetPermissionPackage).also {
            registerReceiver(internetPermission, it)
        }
        val smsBody = findViewById<TextView>(R.id.name)
        smsBody.text = intent.getStringExtra("sms")

        binding.apply {
            saveButton.setOnClickListener {
                val name = findViewById<EditText>(R.id.editTextTextPersonName)
                myEdit.putString(preferenceName, name.text.toString())
                myEdit.apply()
                name.text.clear()
            }

            loadButton.setOnClickListener {
                val storedName = sharedPreferences.getString(preferenceName, "Name")
                name.text = storedName
            }
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

    private fun getPermission(permission: String, permissionCode: Int){
        ActivityCompat.requestPermissions(
            this, arrayOf(permission), permissionCode
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
        unregisterReceiver(internetPermission)
    }
}