package com.wysiwyg.mountcak.ui.home

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Event
import java.util.Collections.reverse

class HomePresenter(private val view: HomeView) {

    private val db = FirebaseDatabase.getInstance().reference

    fun getEventData() {
        view.showLoading()
        db.child("event").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val event: MutableList<Event?> = mutableListOf()
                for (data: DataSnapshot in p0.children) {
                    val e = data.getValue(Event::class.java)
                    event.add(e)
                }
                reverse(event)
                view.hideLoading()
                view.showEventList(event)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}