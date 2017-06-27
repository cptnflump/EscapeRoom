package com.createful.escaperoom.escaperoom

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.annotation.IntDef
import android.util.Log

class MyService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        //throw new UnsupportedOperationException("error");
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("SERVICE", "SERVICE TEST")
        val timer = Timer()

        return super.onStartCommand(intent, flags, startId)
    }
}
