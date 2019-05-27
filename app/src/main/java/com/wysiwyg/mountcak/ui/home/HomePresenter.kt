package com.wysiwyg.mountcak.ui.home

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Mount

class HomePresenter(private val view: HomeView) {

    private val db = FirebaseDatabase.getInstance().reference

    fun getData(mount: MutableList<Mount?>) {
        view.showLoading()
        db.child("mount").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                view.hideLoading()
                for (data: DataSnapshot in p0.children) {
                    val m = data.getValue(Mount::class.java)
                    mount.add(m)
                }
                view.showMountList(mount)
            }
        })
    }
}