package com.example.guestlist

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


const val LAST_GUEST_NAME_KEY = "last-guest-name-bundle-key"

class MainActivity : AppCompatActivity() {

    private lateinit var addGuestButton: Button
    private lateinit var newGuestEditText: EditText
    private lateinit var guestList: TextView
    private lateinit var lastGuestAdded: TextView
    private lateinit var clearGuestListButton: Button

    //val guestNames = mutableListOf<String>()

    private val guestListViewModel: GuestListViewModel by lazy {  // lazy initialization - lambda function won't be called until guestListViewModel is used (increase startup time by only initializing things as you need them)
        ViewModelProvider(this).get(GuestListViewModel::class.java)  // keeps the lifecycle of the activity and the viewModel in sync
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addGuestButton = findViewById(R.id.add_guest_button)
        newGuestEditText = findViewById(R.id.new_guest_input)
        guestList = findViewById(R.id.list_of_guests)
        lastGuestAdded = findViewById(R.id.last_guest_added)
        clearGuestListButton = findViewById(R.id.clear_guests_button)

        addGuestButton.setOnClickListener {
            addNewGuest()
        }

        clearGuestListButton.setOnClickListener {
            guestListViewModel.clearGuestList()
            updateGuestList()
            lastGuestAdded.text = ""
        }

        val savedLastGuestMessage = savedInstanceState?.getString(LAST_GUEST_NAME_KEY)
        lastGuestAdded.text = savedLastGuestMessage

        updateGuestList()  // update from view model - needed when activity is destroyed and recreated.  All of the data in the ViewModel is being saved on rotation also.

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_GUEST_NAME_KEY, lastGuestAdded.text.toString())
    }

    private fun addNewGuest() {
        val newGuestName = newGuestEditText.text.toString()
        if (newGuestName.isNotBlank()) {
//            guestNames.add(newGuestName)
            guestListViewModel.addGuest(newGuestName)
            updateGuestList()
            newGuestEditText.text.clear()
            lastGuestAdded.text = getString(R.string.last_guest_message, newGuestName)
        }
    }

    private fun updateGuestList() {
        //val guestDisplay = guestNames.sorted().joinToString(separator = "\n")
        val guests = guestListViewModel.getSortedGuestNames()
        val guestDisplay = guests.joinToString(separator = "\n")
        guestList.text = guestDisplay
    }

}