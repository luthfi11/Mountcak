package com.wysiwyg.mountcak.ui.home

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.WeatherResponse
import com.wysiwyg.mountcak.util.FirebaseUtil
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class HomePresenter(private val view: HomeView) {

    private val db = FirebaseDatabase.getInstance().reference
    private val apiKey = "0b66142dd36477783c06951d3da8d0cb"

    fun getEventData() {
        view.showLoading()
        db.child("event").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val event: MutableList<Event?> = mutableListOf()
                    p0.children.forEach {
                        val e = it.getValue(Event::class.java)
                        event.add(e)
                    }
                    event.reverse()
                    view.hideLoading()
                    view.showEventList(event)

                    if (event.size == 0) view.emptyEvent()
                    else view.notEmptyEvent()

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun getCurrentWeather(lat: Double, lon: Double) {
        val url = "http://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&mode=json&appid=$apiKey&units=metric"
        getData(url)
    }

    fun getCurrentWeatherByCity() {
        db.child("user").child(FirebaseUtil.currentUser()).child("city")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    var city = p0.getValue(String::class.java)

                    if (city?.contains("Kota ")!!) city = city.replace("Kota ","")
                    if (city.contains("Kab. ")) city = city.replace("Kab. ","")

                    val url = "http://api.openweathermap.org/data/2.5/weather?q=$city,ID&mode=json&appid=$apiKey&units=metric"

                    getData(url)
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    private fun getData(url: String) = doAsync {
        val responseJson = URL(url).readText()
        val data = Gson().fromJson(responseJson, WeatherResponse::class.java)

        uiThread {
            view.showCurrentWeather(data)
        }
    }
}