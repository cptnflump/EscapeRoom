package com.createful.escaperoom.escaperoom

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class TitleScreen : AppCompatActivity(), RoomFragment.OnListFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_screen)

        //this only populates the list if it has not done so already (fixes orientation issues)
        if (savedInstanceState == null) {
            RoomList.populateRooms()
        }
    }


    override fun onListFragmentInteraction(item: Room) {
        val intent = Intent(this, RoomActivity::class.java)
        intent.putExtra("room", item)
        startActivity(intent)
    }
}
