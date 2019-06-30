package com.wysiwyg.mountcak.ui.home

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Event

class HomePresenter(private val view: HomeView) {

    private val db = FirebaseDatabase.getInstance().reference

    fun getEventData() {
        view.showLoading()
        db.child("event").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val event: MutableList<Event?> = mutableListOf()
                    p0.children.forEach {
                        val e = it.getValue(Event::class.java)
                        event.add(e)
                    }
                    event.reverse()
                    view.hideLoading()
                    view.showEventList(event)
                    
                    if (event.size == 0) view.emptyEvent()
                    else view.notEmptyEvent()

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}