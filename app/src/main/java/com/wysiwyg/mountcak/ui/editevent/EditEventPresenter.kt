package com.wysiwyg.mountcak.ui.editevent

import com.google.firebase.database.FirebaseDatabase
import com.wysiwyg.mountcak.data.model.Event
import java.lang.Exception

class EditEventPresenter(private val view: EditEventView) {

    private val db = FirebaseDatabase.getInstance().reference

    fun getData(event: Event?) {
        view.showData(event)
    }

    fun updateData(eid: String, event: Event) {
        try {
            view.showLoading()
            db.child("event").child(eid).setValue(event)
                .addOnSuccessListener {
                    view.hideLoading()
                    view.updateSuccess()
                }
                .addOnFailureListener {
                    view.hideLoading()
                    view.updateFailed()
                }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}