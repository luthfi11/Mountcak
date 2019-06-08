package com.wysiwyg.mountcak.ui.explore

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Mount
import java.lang.Exception

class ExplorePresenter(private val view: ExploreView) {

    private val db = FirebaseDatabase.getInstance().reference

    fun getData() {
        view.showLoading()
        db.child("mount").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val mount: MutableList<Mount?> = mutableListOf()
                    for (data: DataSnapshot in p0.children) {
                        val m = data.getValue(Mount::class.java)
                        mount.add(m)
                    }
                    view.showMountList(mount)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                view.hideLoading()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}