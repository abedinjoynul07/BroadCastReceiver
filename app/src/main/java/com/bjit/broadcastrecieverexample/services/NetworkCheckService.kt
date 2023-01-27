package com.bjit.broadcastrecieverexample.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

class NetworkCheckService : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        val isConnected = activeNetwork?.isConnected == true
        if (!isConnected) {
            isOnline(context)
        } else {
            isOnline(context)
        }
    }
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Toast.makeText(context, "ON CELLULAR NETWORK", Toast.LENGTH_SHORT).show()
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Toast.makeText(context, "ON WIFI NETWORK", Toast.LENGTH_SHORT).show()
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Toast.makeText(context, "ON ETHERNET NETWORK", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show()
        return false
    }

}

