package com.sameershelar.connectivityobserver

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ConnectivityObserver(
    context: Context
) {
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    fun getConnectivityStatus(): Flow<ConnectivityStatus> = callbackFlow {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(ConnectivityStatus("Network available"))
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(ConnectivityStatus("Network unavailable"))
            }
        }

        connectivityManager.requestNetwork(networkRequest, networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}

data class ConnectivityStatus(
    val status: String
)