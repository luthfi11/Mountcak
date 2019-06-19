package com.wysiwyg.mountcak.ui.search

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Mount

class SearchPresenter(private val view: SearchView) {

    private val db = FirebaseDatabase.getInstance().reference.child("mount")

    private var name: String = ""
    private val mountListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            try {
                val mount: MutableList<Mount?> = mutableListOf()
                p0.children.forEach {
                    val m = it.getValue(Mount::class.java)
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
    }

    fun getData(name: String) {
        view.showLoading()
        this.name = name
        db.addValueEventListener(mountListener)
    }

    fun onClose() {
        db.removeEventListener(mountListener)
    }
}