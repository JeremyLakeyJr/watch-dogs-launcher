package com.example.watchdogslauncher.ui.bitchat

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import java.net.ServerSocket

class NsdHelper(private val context: Context) {

    private val nsdManager: NsdManager =
        context.getSystemService(Context.NSD_SERVICE) as NsdManager

    private var registrationListener: NsdManager.RegistrationListener? = null
    private var discoveryListener: NsdManager.DiscoveryListener? = null

    private val serviceType = "_bitchat._tcp."
    private var serviceName = "BitChat"

    fun registerService(port: Int) {
        val serviceInfo = NsdServiceInfo().apply {
            this.serviceName = serviceName
            this.serviceType = serviceType
            this.port = port
        }

        registrationListener = object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(nsdServiceInfo: NsdServiceInfo) {
                serviceName = nsdServiceInfo.serviceName
                Log.d("NsdHelper", "Service registered: $serviceName")
            }

            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e("NsdHelper", "Registration failed: $errorCode")
            }

            override fun onServiceUnregistered(arg0: NsdServiceInfo) {
                Log.d("NsdHelper", "Service unregistered: ${arg0.serviceName}")
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                Log.e("NsdHelper", "Unregistration failed: $errorCode")
            }
        }

        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
    }

    fun discoverServices() {
        discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(regType: String) {
                Log.d("NsdHelper", "Service discovery started")
            }

            override fun onServiceFound(service: NsdServiceInfo) {
                Log.d("NsdHelper", "Service found: ${service.serviceName}")
                // TODO: Resolve the service to get the host and port
            }

            override fun onServiceLost(service: NsdServiceInfo) {
                Log.e("NsdHelper", "Service lost: ${service.serviceName}")
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.i("NsdHelper", "Discovery stopped: $serviceType")
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("NsdHelper", "Discovery failed: Error code:$errorCode")
                nsdManager.stopServiceDiscovery(this)
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("NsdHelper", "Discovery failed: Error code:$errorCode")
                nsdManager.stopServiceDiscovery(this)
            }
        }

        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    fun stopDiscovery() {
        discoveryListener?.let {
            nsdManager.stopServiceDiscovery(it)
        }
    }

    fun unregisterService() {
        registrationListener?.let {
            nsdManager.unregisterService(it)
        }
    }
}
