package com.wysiwyg.mountcak.ui.search

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Mount

class SearchPresenter(private val view: SearchView) {

    private val db = FirebaseDatabase.getInstance().reference

    fun getData(name: String) {
        view.showLoading()
        db.child("mount").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val mount: MutableList<Mount?> = mutableListOf()
                    for (data: DataSnapshot in p0.children) {
                        val m = data.getValue(Mount::class.java)
                        if (m?.mountName?.contains(name, true)!!)
                            mount.add(m)
                    }
                    mount.sortBy { it?.mountName }
                    view.showData(mount)
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