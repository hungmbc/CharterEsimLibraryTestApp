package com.example.esimtestapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.euicc.EuiccManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.charter.esimlibrary.EsimHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val esimHandler = EsimHandler(::onSuccess, ::onFailure)
    private val activationCode: String = "LPA:1\$SM-V4-056-A-GTM.PR.GO-ESIM.COM\$2BAAFB3DE42F05FB2314F6875376A9B1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        esimHandler.init(applicationContext)

        btnDownloadEsim.setOnClickListener {
            esimHandler.downloadEsim(activationCode, true)
        }

        btnDownloadFailed.setOnClickListener {
            esimHandler.downloadEsim(activationCode, false)
        }

        val subsList = getActiveSubscriptions()
        Log.d("TAG_ESIMTESTAPP", subsList.toString())

    }

    private fun onSuccess(result: String) {
        Toast.makeText(this@MainActivity, result, Toast.LENGTH_LONG).show()
        Log.d("TAG_ESIMTESTAPP", result)
    }

    private fun onFailure(result: String) {
        Toast.makeText(this@MainActivity, result, Toast.LENGTH_LONG).show()
        Log.d("TAG_ESIMTESTAPP", "DOWNLOAD FAILED!")
        Log.d("TAG_ESIMTESTAPP", activationCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        esimHandler.onDestroy()
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
}
