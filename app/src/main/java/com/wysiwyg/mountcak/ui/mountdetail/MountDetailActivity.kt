package com.wysiwyg.mountcak.ui.mountdetail

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import com.bumptech.glide.Glide
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.ui.viewphoto.ViewPhotoActivity
import com.wysiwyg.temanolga.utilities.gone
import kotlinx.android.synthetic.main.activity_mount_detail.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.jetbrains.anko.startActivity

class MountDetailActivity : AppCompatActivity(), MountDetailView {

    private lateinit var presenter: MountDetailPresenter
    private lateinit var mount: Mount

    override fun showDetail(mount: Mount?) {
        Glide.with(this).load(mount?.cover).placeholder(R.color.colorMuted).into(imgMountDetail)
        imgMountDetail.onClick { startActivity<ViewPhotoActivity>("photo" to mount?.cover) }

        tvMountName.text = mount?.mountName
        tvMountDesc.text = mount?.description
        tvLocation.text = mount?.location
        tvHeight.text = mount?.height
        tvContact.text = mount?.contact
        tvRoute.text = mount?.route
    }

    override fun callNumber(number: String) {
        btnCall.onClick {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
            startActivity(intent)
        }
    }

    override fun sendMessage(number: String) {
        btnSMS.onClick {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
            startActivity(intent)
        }
    }

    override fun hideCall() {
        btnCall.gone()
        btnSMS.gone()
    }

    override fun showPhoto(url: String, title: String) {
        /*
        val view = layoutInflater.inflate(R.layout.layout_photo, null)
        BottomSheetDialog(this).let {
            it.setContentView(view)
            it.tvTitle.text = title
            it.webPhoto.webViewClient = MyBrowserUtil
            it.webPhoto.settings.javaScriptEnabled = true
            it.webPhoto.settings.setGeolocationEnabled(true)
            it.webPhoto.loadUrl(url)
            it.show()
        }
        */
    }

    override fun showInstagram(ig: String) {
        btnInsta.onClick { browse(ig) }
    }

    override fun showMap(long: Double?, lat: Double?, title: String?) {
        placeMap.getMapAsync { mapboxMap ->
            mapboxMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lat!!, long!!), 9.0
                )
            )
            mapboxMap.addMarker(
                MarkerOptions()
                    .position(LatLng(lat, long))
                    .title(title)
            )
        }

        presenter.onMapTouch()
    }

    override fun onMapTouch() {
        placeMap.onTouch { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                scrollView.requestDisallowInterceptTouchEvent(true)
            } else if (event.action == MotionEvent.ACTION_UP) {
                scrollView.requestDisallowInterceptTouchEvent(false)
            }
        }
    }

    override fun hideInsta() {
        btnInsta.gone()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_mount_detail)

        setSupportActionBar(toolbarMountDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mount = intent.getParcelableExtra("mount")

        placeMap.onCreate(savedInstanceState)

        presenter = MountDetailPresenter(this)
        presenter.getMountDetail(mount)
        presenter.callNumber(mount.contact)
        presenter.sendMessage(mount.contact)
        presenter.getMap(mount.longLat, mount.mountName)
        presenter.viewInstagram(mount.instagram!!)

        //btnPhoto.onClick { presenter.viewPhoto(mount.linkPhotoGM!!, mount.mountName!!) }
    }

    override fun onDestroy() {
        super.onDestroy()
        placeMap.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
