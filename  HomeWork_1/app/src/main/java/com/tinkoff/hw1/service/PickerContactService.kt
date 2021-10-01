package com.tinkoff.hw1.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tinkoff.hw1.util.Constant
import com.tinkoff.hw1.model.Contact

class PickerContactService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val users = loadContacts()
        val intentBroadcast = Intent(Constant.ACTION_GET_CONTACTS)
            .putParcelableArrayListExtra(Constant.EXTRA_KEY_CONTACTS, users)
        val broadcastManager = LocalBroadcastManager.getInstance(baseContext)
        broadcastManager.sendBroadcast(intentBroadcast)
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    private fun loadContacts(): ArrayList<Contact> {
        val userContacts = arrayListOf<Contact>()
        val uri = ContactsContract.Contacts.CONTENT_URI
        val cursor = contentResolver.query(
            uri,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME
        )
        cursor?.moveToFirst()
        while (cursor?.moveToNext() == true) {
            val displayName =
                cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                )
            userContacts.add(
                Contact(contactName = displayName)
            )
        }
        cursor?.close()
        return userContacts
    }
}