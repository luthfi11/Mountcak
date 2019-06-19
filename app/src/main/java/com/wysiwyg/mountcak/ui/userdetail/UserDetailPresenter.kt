package com.wysiwyg.mountcak.ui.userdetail

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.User
import java.lang.Exception

class UserDetailPresenter(private val view: UserDetailView) {

    private val db = FirebaseDatabase.getInstance().reference

    private val userListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            view.hideLoading()
            try {
                val user = p0.getValue(User::class.java)
                view.showUserData(user)
                getUserPost(user?.id)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    private fun getUserPost(uid: String?) {
        db.child("event").orderByChild("userId").equalTo(uid!!).addValueEventListener(object : ValueEventListener{
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

    fun getUserData(uid: String) {
        view.showLoading()
        db.child("user").child(uid).addValueEventListener(userListener)
    }

    fun onClose(uid: String) {
        db.child("user").child(uid).removeEventListener(userListener)
    }
}