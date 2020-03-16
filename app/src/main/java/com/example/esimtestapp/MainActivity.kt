package com.example.esimtestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.charter.esimlibrary.EsimHandler

class MainActivity : AppCompatActivity() {

    private val esimHandler = EsimHandler(::onSuccess, ::onFailure)
    private val activationCode1: String = "LPA:1\$SM-V4-056-A-GTM.PR.GO-ESIM.COM\$9E2F7E12D3B84B1167028E7AB06AD033"
    private val activationCode: String = "LPA:1\$SM-V4-056-A-GTM.PR.GO-ESIM.COM\$2BAAFB3DE42F05FB2314F6875376A9B1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        esimHandler.init(applicationContext)
        esimHandler.downloadEsim(activationCode1)
    }

    private fun onSuccess(result: String) {
        Toast.makeText(this@MainActivity, result, Toast.LENGTH_LONG).show()
        Log.d("TAG_ESIMTESTAPP", result)
    }

    private fun onFailure() {
        Toast.makeText(this@MainActivity, "DOWNLOAD FAILED!", Toast.LENGTH_LONG).show()
        Log.d("TAG_ESIMTESTAPP", "DOWNLOAD FAILED!")
        Log.d("TAG_ESIMTESTAPP", activationCode1)
    }

    override fun onDestroy() {
        super.onDestroy()
        esimHandler.onDestroy()
    }
}
