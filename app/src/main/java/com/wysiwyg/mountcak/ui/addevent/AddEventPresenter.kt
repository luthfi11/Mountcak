package com.wysiwyg.mountcak.ui.addevent

import android.content.Context
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.util.showMountList

class AddEventPresenter(private val view: AddEventView) {

    private val db = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private val eid = db.child("event").push().key
    private val uid = auth.currentUser?.uid

    fun getMountList(ctx: Context, spn: Spinner, tv: TextView) {
        showMountList(ctx, spn, tv, 0)
    }

    fun setID() {
        view.setID(eid, uid)
    }

    fun postEvent(event: Event) {
        try {
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
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}