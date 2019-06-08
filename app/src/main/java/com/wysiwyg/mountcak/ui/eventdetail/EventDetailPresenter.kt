package com.wysiwyg.mountcak.ui.eventdetail

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.User

class EventDetailPresenter(private val view: EventDetailView) {

    private val db = FirebaseDatabase.getInstance().reference

    fun getDetail(event: Event?) {
        view.showEventDetail(event)
    }

    fun mountData(id: String) {
        db.child("mount").child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val mount = p0.getValue(Mount::class.java)
                    view.showMountData(mount)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun userData(id: String) {
        db.child("user").child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val user = p0.getValue(User::class.java)
                    view.showUserData(user)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}