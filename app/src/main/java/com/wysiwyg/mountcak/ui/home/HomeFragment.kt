package com.wysiwyg.mountcak.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.WeatherResponse
import com.wysiwyg.mountcak.ui.addevent.AddEventActivity
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class HomeFragment : Fragment(), HomeView, LocationListener {

    private lateinit var presenter: HomePresenter
    private lateinit var adapter: EventAdapter
    private val event: MutableList<Event?> = mutableListOf()
    private lateinit var locationManager: LocationManager
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    override fun showLoading() {
        srlHome.isRefreshing = true
    }

    override fun hideLoading() {
        srlHome.isRefreshing = false
    }

    @SuppressLint("SetTextI18n")
    override fun showCurrentWeather(weatherResponse: WeatherResponse) {
        try {
            val icon = "http://openweathermap.org/img/wn/${weatherResponse.weather[0].icon}@2x.png"
            Glide.with(context!!).load(icon).into(imgWeather)
        } finally { }
        tvLocationName.text = weatherResponse.name
        tvCurrentWeather.text = weatherResponse.weather[0].main + ", " + weatherResponse.weather[0].description
        tvTemperature.text = "${weatherResponse.main.temp}\u00B0 C"
    }

    override fun showEventList(event: List<Event?>) {
        this.event.clear()
        this.event.addAll(event)
        adapter.notifyDataSetChanged()
    }

    override fun emptyEvent() {
        rvEvent.gone()
        tvEmpty.visible()
    }

    override fun notEmptyEvent() {
        rvEvent.visible()
        tvEmpty.gone()
    }

    override fun onLocationChanged(location: Location?) {
        longitude = location?.longitude!!
        latitude = location.latitude
        getLastWeather(longitude, latitude)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        presenter = HomePresenter(this)
        presenter.getEventData()

        longitude = defaultSharedPreferences.getString("lon", "0.0")!!.toDouble()
        latitude = defaultSharedPreferences.getString("lat", "0.0")!!.toDouble()

        getLastWeather(longitude, latitude)
        setupRecyclerView()
        onAction()
    }

    private fun getLastWeather(longitude: Double, latitude: Double) {
        if ((longitude == 0.0) and (latitude == 0.0)) presenter.getCurrentWeatherByCity()
        else presenter.getCurrentWeather(latitude, longitude)
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(event)
        rvEvent.setHasFixedSize(true)
        rvEvent.layoutManager = LinearLayoutManager(context)
        rvEvent.adapter = adapter
    }

    private fun onAction() {
        btnRefresh.onClick { getLocation() }
        srlHome.onRefresh { presenter.getEventData() }
        fabAdd.onClick { startActivity<AddEventActivity>() }
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        } else {
            val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                longitude = location.longitude
                latitude = location.latitude
                getLastWeather(longitude, latitude)
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0F, this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        defaultSharedPreferences.edit().apply {
            putString("lon", longitude.toString())
            putString("lat", latitude.toString())
        }.apply()
    }
}
