package com.bjit.broadcastrecieverexample

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.bjit.broadcastrecieverexample.services.AirplaneModeChangeReceiver
import com.bjit.broadcastrecieverexample.services.NetworkCheckService

class MainActivity : AppCompatActivity() {
    private lateinit var receiver: AirplaneModeChangeReceiver
    private lateinit var internetPermission : NetworkCheckService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        receiver = AirplaneModeChangeReceiver()
        internetPermission = NetworkCheckService()

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(receiver, it)
        }

        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE").also {
            registerReceiver(internetPermission, it)
        }

        requestSmsPermission()
    }

    private fun requestSmsPermission() {
        val permission = Manifest.permission.RECEIVE_SMS
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1000)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
        unregisterReceiver(internetPermission)
    }


}