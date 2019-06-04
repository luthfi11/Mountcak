package com.wysiwyg.mountcak.ui.addevent

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Event

class AddEventPresenter(private val view: AddEventView) {

    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private val eid = db.child("event").push().key
    private val uid = auth.currentUser?.uid

    fun getMountList() {
        db.child("mount").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val name: MutableList<String?> = mutableListOf()
                val loc: MutableList<String?> = mutableListOf()
                for (data: DataSnapshot in p0.children) {
                    val n = data.child("mountName").getValue(String::class.java)
                    val l = data.child("location").getValue(String::class.java)
                    name.add(n)
                    loc.add(l)
                }
                view.showMountList(name, loc)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun setID() {
        view.setID(eid, uid)
    }

    fun postEvent(event: Event) {
        view.showLoading()
        db.child("event").child(eid!!).setValue(event)
            .addOnSuccessListener {
                view.hideLoading()
                view.successPost()
            }
            .addOnFailureListener {
                view.hideLoading()
                view.postingFail()
            }
    }
}