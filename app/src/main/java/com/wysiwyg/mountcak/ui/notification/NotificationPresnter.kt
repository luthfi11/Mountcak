package com.wysiwyg.mountcak.ui.notification

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Join

class NotificationPresnter(private val view: NotificationView) {

    private val db = FirebaseDatabase.getInstance().reference
    private val uid = FirebaseAuth.getInstance().currentUser?.uid!!

    private val notifListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            try {
                val join: MutableList<Join?> = mutableListOf()
                p0.children.forEach {
                    it.children.forEach { ds ->
                        val data = ds.getValue(Join::class.java)
                        if ((data?.postSender != uid) and
                            (data?.userReqId == uid) and
                            (data?.status != 2)
                        ) {
                            join.add(data)
                        }
                    }
                }
                join.sortBy { it?.confirmTime }
                view.hideLoading()
                view.showNotification(join)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    fun getNotification() {
        view.showLoading()
        db.child("join").addValueEventListener(notifListener)
    }
}