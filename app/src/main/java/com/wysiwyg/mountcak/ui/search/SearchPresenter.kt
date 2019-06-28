package com.wysiwyg.mountcak.ui.search

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.Rental

class SearchPresenter(private val view: SearchView, private val type: Int?) {

    private val db = FirebaseDatabase.getInstance().reference
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
                view.showMountData(mount)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            view.hideLoading()
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    private val rentalListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            try {
                val rental: MutableList<Rental?> = mutableListOf()
                p0.children.forEach {
                    val r = it.getValue(Rental::class.java)
                    if (r?.storeName?.contains(name, true)!!)
                        rental.add(r)
                }
                rental.sortBy { it?.storeName }
                view.showRentalData(rental)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            view.hideLoading()
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    private fun getMountData(name: String) {
        view.showLoading()
        this.name = name
        db.child("mount").addValueEventListener(mountListener)
    }

    private fun getRentalData(name: String) {
        view.showLoading()
        this.name = name
        db.child("rental").addValueEventListener(rentalListener)
    }

    fun search(name: String) {
        if (type == 1) getMountData(name)
        else if (type == 2) getRentalData(name)
    }

    fun onClose() {
        if (type == 1) db.child("mount").removeEventListener(mountListener)
        else if (type == 2) db.child("rental").removeEventListener(rentalListener)
    }
}