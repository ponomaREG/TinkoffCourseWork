package com.tinkoff.hw1.activity

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.hw1.util.Constant
import com.tinkoff.hw1.model.Contact
import com.tinkoff.hw1.adapter.ContactAdapter
import com.tinkoff.hw1.R

class MainActivity : AppCompatActivity() {

    companion object{
        const val STATE_CONTACTS = "STATE_CONTACTS"
    }

    private lateinit var buttonMoveToSecondActivity: View
    private lateinit var recyclerListWithContacts: RecyclerView

    private val adapterForContacts = ContactAdapter()
    private val secondActivityResult = getRegisterForSecondActivityResult()
    private val permissionsActivityResult = getRegisterForPermissions(
            actionIfGranted = {
                secondActivityResult.launch(SecondActivity.getIntent(this))
            },
            actionNotGranted = {
                showToast(getString(R.string.error_permission_not_given))
            }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        attachListeners()
        attachAdapters()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val contacts = adapterForContacts.getItems()
        if(contacts.isNotEmpty()) outState.putParcelableArrayList(
                STATE_CONTACTS,
                ArrayList(contacts)
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val contacts: List<Contact> =
                savedInstanceState
                        .getParcelableArrayList<Contact>(STATE_CONTACTS)?.toList() ?: return
        adapterForContacts.setItems(contacts)
    }

    private fun ueOnButtonMoveToSecondActivityClick(){
        permissionsActivityResult.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun findViews(){
        buttonMoveToSecondActivity =
                findViewById(R.id.main_button_move_to_second_activity)
        recyclerListWithContacts =
                findViewById(R.id.main_rv_contacts)

    }

    private fun attachListeners(){
        buttonMoveToSecondActivity.setOnClickListener {
            ueOnButtonMoveToSecondActivityClick()
        }
    }

    private fun attachAdapters(){
        recyclerListWithContacts.adapter = adapterForContacts
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showContacts(contacts: List<Contact>) {
        adapterForContacts.setItems(contacts)
    }

    private fun getRegisterForSecondActivityResult() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val contacts: ArrayList<Contact> =
                    result.data?.
                    getParcelableArrayListExtra(
                            Constant.EXTRA_KEY_CONTACTS
                    ) ?: return@registerForActivityResult
                showContacts(contacts)
            }
        }

    private fun getRegisterForPermissions(
        actionIfGranted: () -> Unit,
        actionNotGranted: () -> Unit
    ) =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
            if(granted) actionIfGranted()
            else actionNotGranted()
        }
}