package com.wysiwyg.mountcak.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
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
import org.jetbrains.anko.support.v4.toast

class HomeFragment : Fragment(), HomeView {

    private lateinit var presenter: HomePresenter
    private lateinit var adapter: EventAdapter
    private val event: MutableList<Event?> = mutableListOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private var location: Location? = null
    private lateinit var imgWeather: ImageView

    override fun showLoading() {
        srlHome.isRefreshing = true
    }

    override fun hideLoading() {
        srlHome.isRefreshing = false
    }

    @SuppressLint("SetTextI18n")
    override fun showCurrentWeather(weatherResponse: WeatherResponse) {
        activity?.runOnUiThread {
            val icon = "http://openweathermap.org/img/wn/${weatherResponse.weather[0].icon}@2x.png"
            Glide.with(activity!!).load(icon).into(imgWeather)
        }

        tvLocationName.text = weatherResponse.name
        tvCurrentWeather.text =
            weatherResponse.weather[0].main + ", " + weatherResponse.weather[0].description
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        imgWeather = view.findViewById(R.id.imgWeather)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbarHome)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        presenter = HomePresenter(this)
        presenter.getEventData()

        val longitude = defaultSharedPreferences.getString("lon", "0.0")!!.toDouble()
        val latitude = defaultSharedPreferences.getString("lat", "0.0")!!.toDouble()

        getLastWeather(longitude, latitude)
        setupRecyclerView()
        onAction()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
        rvEvent.isNestedScrollingEnabled = false
    }

    private fun onAction() {
        btnRefresh.onClick { getLastLocation() }
        srlHome.onRefresh { presenter.getEventData() }
        fabAdd.onClick { startActivity<AddEventActivity>() }
    }

    override fun onDestroy() {
        super.onDestroy()
        val longitude = location?.longitude
        val latitude = location?.latitude
        defaultSharedPreferences.edit().apply {
            putString("lon", longitude.toString())
            putString("lat", latitude.toString())
        }.apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            getLastLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                toast("Permission Denied")
            }
        }
    }

    private fun onGetLastLocation(location: Location?) {
        if (location != null) {
            this.location = location
            try {
               getLastWeather(location.latitude, location.longitude)
            } catch (e: Exception) {
                toast("Connection Error")
            }
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
    }

    private var locationRequest: LocationRequest? = null
    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestLocationPermission()
            } else {
                requestLocationPermission()
            }
        } else {
            if (locationRequest == null) {
                locationRequest = LocationRequest.create()?.apply {
                    interval = 10000
                    fastestInterval = 5000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                if (locationRequest != null) {
                    requestTurnOnLocation()
                }
            } else {
                requestTurnOnLocation()
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                onGetLastLocation(location)
                break
            }
            fusedLocationClient.removeLocationUpdates(this)
        }
    }

    @SuppressLint("MissingPermission")
    fun requestTurnOnLocation() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(activity!!)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    startIntentSenderForResult(exception.resolution.intentSender, 1, null, 0, 0, 0, null)
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }
}
