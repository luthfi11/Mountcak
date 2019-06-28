package com.wysiwyg.mountcak.ui.userdetail

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Join
import com.wysiwyg.mountcak.data.model.User

class UserDetailPresenter(private val view: UserDetailView, private val uid: String) {

    private val db = FirebaseDatabase.getInstance().reference

    private val userListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            view.hideLoading()
            try {
                val user = p0.getValue(User::class.java)
                view.showUserData(user)
                getUserPost()
                getUserTrip()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    private fun getUserPost() {
        db.child("event").orderByChild("userId").equalTo(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val event: MutableList<Event?> = mutableListOf()
                    p0.children.forEach {
                        val e = it.getValue(Event::class.java)
                        event.add(e)
                    }
                    event.reverse()
                    view.showUserPost(event)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun getUserTrip() {
        db.child("join").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val eid = mutableListOf<String?>()
                p0.children.forEach {
                    it.children.forEach { data ->
                        val join = data.getValue(Join::class.java)
                        if (join?.userReqId == uid) {
                            eid.add(join.eventId)
                        }
                    }
                }
                eid.reverse()
                userTripData(eid)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun userTripData(eid: MutableList<String?>) {
        val listTrip = mutableListOf<Event?>()
        if (eid.size == 0) view.showUserTrip(listTrip)
        else {
            eid.forEach {
                db.child("event").child(it!!).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        val data = p0.getValue(Event::class.java)
                        listTrip.add(data)
                        view.showUserTrip(listTrip)
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }
        }
    }

    fun getUserData() {
        view.showLoading()
        db.child("user").child(uid).addValueEventListener(userListener)
    }

    fun onClose() {
        db.child("user").child(uid).removeEventListener(userListener)
    }
}