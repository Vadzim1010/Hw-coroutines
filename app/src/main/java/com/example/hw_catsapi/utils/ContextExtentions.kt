package com.example.hw_catsapi.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import com.example.hw_catsapi.CatsApplication
import com.example.hw_catsapi.repository.CatsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

val Context.onNetworkChanges: Flow<Boolean>
    get() = callbackFlow {
        val connectivityManager = getSystemService<ConnectivityManager>()

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        checkNotNull(connectivityManager) { "connectivity manager is $connectivityManager" }

        connectivityManager.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    trySend(false)
                }
            }
        )
        awaitClose()
    }


val Context.repository: CatsRepository
    get() = when (this) {
        is CatsApplication -> catsRepository
        else -> applicationContext.repository
    }