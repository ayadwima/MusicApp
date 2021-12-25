package com.example.musicapp

import android.app.Application
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket


class SocketCreate : Application() {

    private var mSocket: Socket? = IO.socket("http://192.168.1.90:4000")


    fun getSocket(): Socket? {
        return mSocket
    }
}