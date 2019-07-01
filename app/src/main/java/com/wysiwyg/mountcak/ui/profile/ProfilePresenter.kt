package com.wysiwyg.mountcak.ui.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Join
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.User

class ProfilePresenter(private val view: ProfileView) {

    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid!!
    private val db = FirebaseDatabase.getInstance().reference

    fun getUserData() {
        view.showLoading()
        db.child("user").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val user = p0.getValue(User::class.java)
                    view.hideLoading()
                    view.showData(user)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun getUserPost() {
        db.child("event").orderByChild("userId").equalTo(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        val event: MutableList<Event?> = mutableListOf()
                        p0.children.forEach {
                            val e = it.getValue(Event::class.java)
                            event.add(e)
                        }
                        event.reverse()
                        view.showEvent(event)

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    fun getUserTrip() {
        val eid = mutableListOf<String?>()
        db.child("join").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    eid.clear()
                    p0.children.forEach {
                        it.children.forEach { data ->
                            val join = data.getValue(Join::class.java)
                            if ((join?.userReqId == uid) and (join?.status == 1)) {
                                if (data.exists())
                                    eid.add(join?.eventId)
                            }
                        }
                    }
                    eid.reverse()
                    userTripData(eid)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun userTripData(eid: MutableList<String?>) {
        val listTrip = mutableListOf<Event?>()
        if (eid.size == 0) view.showTrip(listTrip)
        else {
            eid.forEach {
                db.child("event").child(it!!).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        try {
                            val data = p0.getValue(Event::class.java)
                            listTrip.add(data)
                            view.showTrip(listTrip)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }
        }
    }

    fun getUserFav() {
        db.child("like").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val mId = mutableListOf<String?>()
                    p0.children.forEach {
                        val data = it.getValue(String::class.java)
                        mId.add(data)
                    }
                    userFavData(mId)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun userFavData(mId: MutableList<String?>) {
        val listMount = mutableListOf<Mount?>()
        if (mId.size == 0) view.showMount(listMount)
        else {
            mId.forEach {
                db.child("mount").child(it!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        try {
                            val mount = p0.getValue(Mount::class.java)
                            listMount.add(mount)
                            view.showMount(listMount)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }
        }
    }

    fun logout() {
        auth.signOut().also {
            view.doLogout()
        }
    }
}