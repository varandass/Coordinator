package ua.varandas.coordinator.firebase.ads

import android.content.Context
import android.net.ConnectivityManager
import ua.varandas.coordinator.MyAppClass


object URLConnection {

    val isNetAvailable: Boolean
        get() {
            val connectivityManager = MyAppClass.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            return if (connectivityManager != null) {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                activeNetworkInfo != null && activeNetworkInfo.isConnected
            } else {
                false
            }
        }
}