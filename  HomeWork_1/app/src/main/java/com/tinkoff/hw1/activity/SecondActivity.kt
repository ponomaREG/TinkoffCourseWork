package com.tinkoff.hw1.activity

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tinkoff.hw1.util.Constant
import com.tinkoff.hw1.R
import com.tinkoff.hw1.service.ServicePickerContact

class SecondActivity : AppCompatActivity() {

    companion object{
        fun getIntent(context: Context) =
            Intent(context, SecondActivity::class.java)
    }

    private lateinit var buttonStartService: Button
    private lateinit var broadcastManager: LocalBroadcastManager

    private val contactsReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            if(intent.hasExtra(Constant.EXTRA_KEY_CONTACTS)){
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_second)
        broadcastManager = LocalBroadcastManager.getInstance(this)
        findViews()
        attachListeners()
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(Constant.ACTION_GET_CONTACTS)
        broadcastManager.registerReceiver(contactsReceiver, intentFilter)
    }

    override fun onStop() {
        broadcastManager.unregisterReceiver(contactsReceiver)
        super.onStop()
    }

    private fun ueOnButtonStartServiceClick(){
        startService(Intent(this, ServicePickerContact::class.java))
        buttonStartService.isEnabled = false
        buttonStartService.text = getString(R.string.second_button_status_service_is_running)
    }

    private fun findViews(){
        buttonStartService = findViewById(R.id.second_button_start_service)
    }

    private fun attachListeners(){
        buttonStartService.setOnClickListener {
            ueOnButtonStartServiceClick()
        }
    }
}