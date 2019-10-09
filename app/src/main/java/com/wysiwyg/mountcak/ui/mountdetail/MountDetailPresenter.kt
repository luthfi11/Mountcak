package com.wysiwyg.mountcak.ui.mountdetail

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.wysiwyg.mountcak.data.model.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class MountDetailPresenter(private val view: MountDetailView, private val id: String) {

    private val db = FirebaseDatabase.getInstance().reference
    private val uid = FirebaseAuth.getInstance().currentUser?.uid!!

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
                getForecast(mount?.longLat)
                callNumber(mount?.contact)
                sendMessage(mount?.contact)
                getMap(mount?.longLat, mount?.mountName)
                viewInstagram(mount?.instagram!!)
                checkLike()
                getVideo(mount.mountName)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

    private val expListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {
            try {
                val review = mutableListOf<Review?>()
                for ((counter, i: DataSnapshot) in p0.children.withIndex()) {
                    if (counter < 3) {
                        val data = i.getValue(Review::class.java)
                        review.add(data)
                    } else break
                }

                if (review.size == 0) view.emptyReview()
                view.showUserReview(review)

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

    private fun checkLike() {
        db.child("like").child(uid).child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) view.isLiked()
                else view.isNotLiked()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun getMountDetail() {
        view.showLoading()
        db.child("experience").child(id).addValueEventListener(expListener)
        db.child("mount").child(id).addValueEventListener(mountListener)
    }

    fun getForecast(lonLat: String?) {
        view.showForecastLoading()

        val coord = lonLat!!.split(",")
        val apiKey = "0b66142dd36477783c06951d3da8d0cb"
        val url =
            "http://api.openweathermap.org/data/2.5/forecast?lat=${coord[0]}&lon=${coord[1]}&mode=json&appid=$apiKey&units=metric"

        doAsync {
            val responseJson = URL(url).readText()
            val data = Gson().fromJson(responseJson, ForecastResponse::class.java)

            uiThread {
                view.hideForecastLoading()
                view.showForecast(data.list)
            }
        }
    }

    fun likeMount() {
        db.child("like").child(uid).child(id).setValue(id).addOnSuccessListener {
            view.successLike()
            view.isLiked()
        }
    }

    fun dislikeMount() {
        db.child("like").child(uid).child(id).removeValue()
            .addOnSuccessListener { view.isNotLiked() }
    }

    fun onClose() {
        db.child("mount").child(id).removeEventListener(mountListener)
        db.child("experience").child(id).removeEventListener(expListener)
    }

    fun getVideo(mount: String?) {
        view.showVideoLoading()

        val api = "AIzaSyCY2dZOC9wJy1zlHIFn02GhBUbuNAVP8ug"
        val query = mount?.replace(" ", "+")
        val url =
            "https://www.googleapis.com/youtube/v3/search?part=snippet&q=$query&type=video&maxResults=10&key=$api"
        doAsync {
            val responseJson = URL(url).readText()
            val data = Gson().fromJson(responseJson, VideResult::class.java)

            uiThread {
                view.hideVideoLoading()
                view.showVideo(data.items)
            }
        }
    }
}