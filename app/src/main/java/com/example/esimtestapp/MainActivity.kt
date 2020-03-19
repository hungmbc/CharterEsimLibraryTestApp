package com.example.esimtestapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.telephony.euicc.EuiccManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.charter.esimlibrary.EsimHandler
import com.charter.esimlibrary.OnEsimDownloadListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnEsimDownloadListener {

    private lateinit var esimHandler: EsimHandler
    //private val activationCode1: String = "LPA:1\$SM-V4-056-A-GTM.PR.GO-ESIM.COM\$47379860C27F12B99B49F9C3E214716E"
    private val activationCode: String = "LPA:1\$SM-V4-056-A-GTM.PR.GO-ESIM.COM\$540770E229B4D6AE0A54EB75619F5E81"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        esimHandler = EsimHandler(applicationContext)
        esimHandler.init(this)

        btnDownloadEsim.setOnClickListener {
            esimHandler.downloadEsim(activationCode)
        }

        val subsList = getActiveSubscriptions()
        Log.d("TAG_ESIMTESTAPP", subsList.toString())

//        checkEuiccInfo()
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.Q) // Gets a list of the active subscriptions from the SubscriptionManager
    private fun getActiveSubscriptions(): List<SubscriptionInfo?>? {
        val subscriptionManager =
            applicationContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        return subscriptionManager.activeSubscriptionInfoList
            ?: return null
    }

    // Checks the EID of the device
    private fun checkEuiccInfo() {
        val mgr = applicationContext.getSystemService(Context.EUICC_SERVICE) as EuiccManager
        if (mgr.isEnabled) {
            val eid = mgr.eid
            Toast.makeText(applicationContext, applicationContext.getString(R.string.eid_is) + eid, Toast.LENGTH_LONG)
                .show()
            Log.i("TAG_ESIM", applicationContext.getString(R.string.eid_is) + eid)
        } else {
            Toast.makeText(applicationContext, applicationContext.getString(R.string.eid_not_present), Toast.LENGTH_LONG)
                .show()
            Log.i("TAG_ESIM", applicationContext.getString(R.string.eid_not_present))
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun switchtMultiSimConfiguration() {
        try {
            Log.i("TAG_ESIM", "Switching multisim config to 2 sims")
            val telephonyManager =
                applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.switchMultiSimConfig(2)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFailure(result: String) {
        Toast.makeText(this@MainActivity, result, Toast.LENGTH_LONG).show()
        Log.d("TAG_ESIMTESTAPP", "DOWNLOAD FAILED!")
        Log.d("TAG_ESIMTESTAPP", activationCode)
    }

    override fun onSuccess(result: String) {
        Toast.makeText(this@MainActivity, result, Toast.LENGTH_LONG).show()
        Log.d("TAG_ESIMTESTAPP", result)
    }
}
