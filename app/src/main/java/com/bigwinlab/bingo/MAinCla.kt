package com.bigwinlab.bingo

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MAinCla: Application() {
    companion object {
        const val AppsFlyerDevKey = "4XnsA7LMrJy3AfFRHZ7G4k"
        const val OneSignalAppId = "162612c4-abbe-4fab-9185-eb6b1c69d762"

        val fowerhfhc = "http://amethyst"
        val ovjervncds = "adventure.xyz/apps.txt"

        var lknfr: String? = ""
        var G1: String? = "c11"
        var H1: String? = "d11"
        var CH: String? = "check"

    }

    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch(Dispatchers.IO) {
            kfherfhhf(context = applicationContext)
        }
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(OneSignalAppId)
    }

    private suspend fun kfherfhhf(context: Context) {
        val advertisingInfo = Adv(context)
        val idInfo = advertisingInfo.getAdvertisingId()

        val prefs = getSharedPreferences("SP", Application.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString(lknfr, idInfo)
        editor.apply()
    }

}

class Adv (context: Context) {
    private val adInfo = AdvertisingIdClient(context.applicationContext)

    suspend fun getAdvertisingId(): String =
        withContext(Dispatchers.IO) {
            adInfo.start()
            val adIdInfo = adInfo.info
            Log.d("getAdvertisingId = ", adIdInfo.id.toString())
            adIdInfo.id
        }
}