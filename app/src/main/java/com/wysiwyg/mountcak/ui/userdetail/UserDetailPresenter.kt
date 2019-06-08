package com.wysiwyg.mountcak.ui.userdetail

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.User
import java.lang.Exception

class UserDetailPresenter(private val view: UserDetailView) {

    private val db = FirebaseDatabase.getInstance().reference

    fun getUserData(uid: String) {
        view.showLoading()
        db.child("user").child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val user = p0.getValue(User::class.java)
                    view.showUserData(user)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                view.hideLoading()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        db.child("event").orderByChild("userId").equalTo(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val event: MutableList<Event?> = mutableListOf()
                    for (data: DataSnapshot in p0.children) {
                        val e = data.getValue(Event::class.java)
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
}