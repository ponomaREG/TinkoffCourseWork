package com.tinkoff.hw1.activity

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.savedstate.SavedStateRegistry
import com.tinkoff.hw1.util.Constant
import com.tinkoff.hw1.model.Contact
import com.tinkoff.hw1.adapter.ContactAdapter
import com.tinkoff.hw1.R
import com.tinkoff.hw1.util.registerForPermission
import com.tinkoff.hw1.util.showToast

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SIS_CONTACTS = "SIS_CONTACTS"
        private const val KEY_PROVIDER = "KEY_PROVIDER"
    }

    private lateinit var buttonMoveToSecondActivity: View
    private lateinit var recyclerListWithContacts: RecyclerView

    private val adapterForContacts = ContactAdapter()
    private val secondActivityResult = getRegisterForSecondActivityResult()
    private val permissionsActivityResult = registerForPermission { granted ->
        if(granted) secondActivityResult.launch(SecondActivity.getIntent(this))
        else showToast(getString(R.string.error_permission_not_given))
    }
    private val savedStateProvider = SavedStateRegistry.SavedStateProvider {
        Bundle().apply {
            putParcelableArrayList(SIS_CONTACTS, ArrayList(adapterForContacts.getItems()))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        attachListeners()
        attachAdapters()
        savedStateRegistry.registerSavedStateProvider(KEY_PROVIDER, savedStateProvider)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val contacts: List<Contact> = savedStateRegistry
            .consumeRestoredStateForKey(KEY_PROVIDER)
            ?.getParcelableArrayList(SIS_CONTACTS)
            ?: emptyList()
        showContacts(contacts)
    }

    private fun ueOnButtonMoveToSecondActivityClick() {
        permissionsActivityResult.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun findViews() {
        buttonMoveToSecondActivity =
            findViewById(R.id.main_button_move_to_second_activity)
        recyclerListWithContacts =
            findViewById(R.id.main_rv_contacts)
    }

    private fun attachListeners() {
        buttonMoveToSecondActivity.setOnClickListener {
            ueOnButtonMoveToSecondActivityClick()
        }
    }

    private fun attachAdapters() {
        recyclerListWithContacts.adapter = adapterForContacts
    }

    private fun showContacts(contacts: List<Contact>) {
        adapterForContacts.setItems(contacts)
    }

    private fun getRegisterForSecondActivityResult() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val contacts: ArrayList<Contact> =
                    result.data?.getParcelableArrayListExtra(
                        Constant.EXTRA_KEY_CONTACTS
                    ) ?: return@registerForActivityResult
                showContacts(contacts)
            }
        }
}