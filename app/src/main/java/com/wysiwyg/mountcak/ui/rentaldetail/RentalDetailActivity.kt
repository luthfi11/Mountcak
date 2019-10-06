package com.wysiwyg.mountcak.ui.rentaldetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Rental
import com.wysiwyg.mountcak.ui.viewphoto.ViewPhotoActivity
import com.wysiwyg.temanolga.utilities.gone
import kotlinx.android.synthetic.main.activity_rental_detail.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.jetbrains.anko.startActivity

class RentalDetailActivity : AppCompatActivity(), RentalDetailView {

    private lateinit var presenter: RentalDetailPresenter

    override fun showRentalDetail(rental: Rental?) {
        Glide.with(this).load(rental?.storePict).placeholder(R.mipmap.ic_launcher_round).into(imgStorePict)
        Glide.with(this).load(rental?.priceList).placeholder(R.color.colorMuted).into(imgPriceList)

        tvStoreName.text = rental?.storeName
        tvCity.text = "${rental?.city}, ${rental?.region}"
        tvAddress.text = rental?.address
        tvOpenHour.text = rental?.openHour
        tvContact.text = rental?.contact
        tvInstagram.text = rental?.instagram

        if (rental?.storePict != "")
            imgStorePict.onClick { startActivity<ViewPhotoActivity>("photo" to rental?.storePict) }
        imgPriceList.onClick { startActivity<ViewPhotoActivity>("photo" to rental?.priceList) }
    }

    override fun callNumber(number: String?) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
    }

    override fun sendMessage(number: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
        startActivity(intent)
    }

    override fun viewInstagram(id: String?) {
        browse("https://instagram.com/$id")
    }

    override fun showMap(long: Double?, lat: Double?, title: String?) {
        storeMap.getMapAsync { mapboxMap ->
            mapboxMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lat!!, long!!), 13.0
                )
            )
            mapboxMap.addMarker(
                MarkerOptions()
                    .position(LatLng(lat, long))
                    .title(title)
            )
            mapboxMap.addOnMapClickListener {
                presenter.openGoogleMaps()
            }
        }
    }

    override fun onMapTouch() {
        storeMap.onTouch { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> scrollView.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP -> scrollView.requestDisallowInterceptTouchEvent(false)
            }
        }
    }

    override fun openGoogleMaps(link: String?) {
        browse(link!!)
    }

    override fun hideContact() {
        tvContact.text = "-"
        btnCall.gone()
        btnSMS.gone()
    }

    override fun hideInstagram() {
        tvInstagram.text = "-"
        btnInstagram.gone()
    }

    override fun hidePriceList() {
        tv5.gone()
        imgPriceList.gone()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_rental_detail)
        setSupportActionBar(toolbarRentalDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val rental = intent.getParcelableExtra<Rental>("rental")

        presenter = RentalDetailPresenter(this, rental)
        presenter.getRentalDetail()

        btnCall.onClick { presenter.callNumber() }
        btnSMS.onClick { presenter.sendMessage() }
        btnInstagram.onClick { presenter.viewInstagram() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        storeMap.onDestroy()
    }
}
