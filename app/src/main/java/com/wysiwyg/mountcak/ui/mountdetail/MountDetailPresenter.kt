package com.wysiwyg.mountcak.ui.mountdetail

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Mount

class MountDetailPresenter(private val view: MountDetailView) {

    private val db = FirebaseDatabase.getInstance().reference.child("mount")

    private val mountListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {
            try {
                val mount: Mount? = p0.getValue(Mount::class.java)
                view.hideLoading()
                view.showDetail(mount)

                if (mount?.instagram == null) {
                    view.hideInsta()
                }
                callNumber(mount?.contact)
                sendMessage(mount?.contact)
                getMap(mount?.longLat, mount?.mountName)
                viewInstagram(mount?.instagram!!)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

    private fun callNumber(number: String?) {
        if (number != "-")
            view.callNumber(number!!)
        else
            view.hideCall()
    }

    private fun sendMessage(number: String?) {
        if (number != "-") {
            view.sendMessage(number!!)
        }
    }

    private fun viewPhoto(url: String, title: String) {
        view.showPhoto(url, title)
    }

    private fun viewInstagram(ig: String) {
        var url = "https://instagram.com/"

        if (ig != "-") {
            url += ig.replace("@", "")
            view.showInstagram(url)
        } else view.hideInsta()
    }

    private fun getMap(longLat: String?, title: String?) {
        val ll = longLat?.split(",")
        view.showMap(ll!![1].toDouble(), ll[0].toDouble(), title)
        onMapTouch()
    }

    private fun onMapTouch() {
        view.onMapTouch()
    }

    fun getMountDetail(id: Int) {
        view.showLoading()
        db.child(id.toString()).addValueEventListener(mountListener)
    }

    fun onClose(id: Int) {
        db.child(id.toString()).removeEventListener(mountListener)
    }
}