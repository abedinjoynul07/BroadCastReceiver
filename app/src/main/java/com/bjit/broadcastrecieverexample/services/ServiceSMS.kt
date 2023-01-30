package com.bjit.broadcastrecieverexample.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.Toast

class ServiceSMS : BroadcastReceiver() {
    override fun onReceive(contxt: Context, intent: Intent) {
        if (!intent.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return
        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        extractMessages.forEach {
            Toast.makeText(contxt, it.displayMessageBody, Toast.LENGTH_SHORT).show()
            intent.putExtra("sms", it.displayMessageBody)
        }
    }
}