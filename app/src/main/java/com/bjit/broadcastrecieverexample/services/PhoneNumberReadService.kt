package com.bjit.broadcastrecieverexample.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.RequiresApi

class PhoneNumberReadService : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val state = intent!!.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_NETWORK_COUNTRY)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                Toast.makeText(
                    context, incomingNumber, Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}