package com.wysiwyg.mountcak.ui.eventdetail

import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.User
import org.jetbrains.anko.textResource
import java.text.NumberFormat
import java.util.*

class EventDetailPresenter(private val view: EventDetailView) {

    private val db = FirebaseDatabase.getInstance().reference
    private val user = FirebaseAuth.getInstance().currentUser!!.uid

    private fun checkPoster(event: Event) {
        if (event.userId == user) view.showEditButton(event)
    }

    private val eventListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {
            view.hideLoading()
            try {
                val event = p0.getValue(Event::class.java)
                view.showEventDetail(event)

                mountData(event?.mountId!!)
                userData(event.userId!!)
                checkPoster(event)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }

    private fun mountData(id: String) {
        db.child("mount").child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val mount = p0.getValue(Mount::class.java)
                    view.showMountData(mount)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun userData(id: String) {
        db.child("user").child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val user = p0.getValue(User::class.java)
                    view.showUserData(user)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun getEventDetail(eid: String) {
        view.showLoading()
        db.child("event").child(eid).addValueEventListener(eventListener)
    }

    fun checkCost(cost: Int?, tv: TextView) {
        return if (cost == 0) {
            tv.textResource = R.string.free
        } else {
            val local = Locale("in", "ID")
            val currency = NumberFormat.getCurrencyInstance(local)
            tv.text = currency.format(cost).toString()
        }
    }

    fun onClose(eid: String) {
        db.child("event").child(eid).removeEventListener(eventListener)
    }
}