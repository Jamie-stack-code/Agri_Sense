package com.example.agri_sense.utils

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor() {

    private var socket: Socket? = null
    private val TAG = "AgriSenseSocket"

    init {
        try {
            val options = IO.Options()
            options.forceNew = true
            options.reconnection = true
            
            // Use BuildConfig.SOCKET_URL to allow testing on physical devices or different networks
            socket = IO.socket(com.example.agri_sense.BuildConfig.SOCKET_URL, options)
            
            socket?.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "⚡ Neural Link Established with Server")
            }
            
            socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e(TAG, "❌ Connection Error: ${args.getOrNull(0)}")
            }

            socket?.connect()
            Log.d(TAG, "📡 Initiating Connection Handshake...")
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Initialization Failed", e)
        }
    }

    fun connect() {
        if (socket?.connected() == false) {
            socket?.connect()
        }
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun emit(event: String, data: Any) {
        Log.d(TAG, "📤 Emitting [$event]: $data")
        if (data is Map<*, *>) {
            socket?.emit(event, JSONObject(data))
        } else {
            socket?.emit(event, data)
        }
    }

    fun on(event: String, listener: (Array<out Any>) -> Unit) {
        socket?.on(event) { args ->
            Log.d(TAG, "📥 Received [$event]")
            listener(args)
        }
    }
}
