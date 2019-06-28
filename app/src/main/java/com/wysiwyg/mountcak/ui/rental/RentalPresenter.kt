package com.wysiwyg.mountcak.ui.rental

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Rental

class RentalPresenter(private val view: RentalView) {

    private val db = FirebaseDatabase.getInstance().reference.child("rental")

    fun getRentalStore() {
        view.showLoading()
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val rent = mutableListOf<Rental?>()
                    p0.children.forEach {
                        val data = it.getValue(Rental::class.java)
                        rent.add(data)
                    }

                    view.hideLoading()
                    view.showData(rent)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}