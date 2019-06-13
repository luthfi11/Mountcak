package com.wysiwyg.mountcak.ui.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.User

class ProfilePresenter(private val view: ProfileView) {

    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid!!
    private val db = FirebaseDatabase.getInstance().reference

    fun getUserData() {
        view.showLoading()
        db.child("user").child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val user = p0.getValue(User::class.java)
                    view.hideLoading()
                    view.showData(user)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun getUserPost() {
        db.child("event").orderByChild("userId").equalTo(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val event: MutableList<Event?> = mutableListOf()
                    for (data: DataSnapshot in p0.children) {
                        val e = data.getValue(Event::class.java)
                        event.add(e)
                    }
                    event.reverse()
                    view.showEvent(event)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun logout() {
        auth.signOut().also {
            view.doLogout()
        }
    }
}