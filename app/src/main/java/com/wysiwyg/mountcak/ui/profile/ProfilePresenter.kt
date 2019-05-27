package com.wysiwyg.mountcak.ui.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfilePresenter(private val view: ProfileView) {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    fun getUserData() {
        view.showLoading()
        db.child("user").child(auth.currentUser?.uid!!).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val name = p0.child("name").getValue(String::class.java)
                    val city = p0.child("city").getValue(String::class.java)
                    view.hideLoading()
                    view.showData(name, city)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        })
    }
}