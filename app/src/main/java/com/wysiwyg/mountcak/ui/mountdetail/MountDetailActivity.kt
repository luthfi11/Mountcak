package com.wysiwyg.mountcak.ui.mountdetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Experience
import com.wysiwyg.mountcak.data.model.Forecast
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.SearchResult
import com.wysiwyg.mountcak.ui.experience.UserExperienceActivity
import com.wysiwyg.mountcak.ui.viewphoto.ViewPhotoActivity
import com.wysiwyg.temanolga.utilities.gone
import kotlinx.android.synthetic.main.activity_mount_detail.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh

class MountDetailActivity : AppCompatActivity(), MountDetailView {

    private lateinit var presenter: MountDetailPresenter
    private lateinit var linkGoogleMaps: String
    private lateinit var adapter: ForecastAdapter
    private lateinit var experienceAdapter: ExperienceAdapter
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var videoAdapter: VideoAdapter
    private val forecast = mutableListOf<Forecast?>()
    private val experience = mutableListOf<Experience?>()
    private val photo = mutableListOf<String?>()
    private val video = mutableListOf<SearchResult?>()

    override fun showLoading() {
        srlMountDetail.isRefreshing = true
    }

    override fun hideLoading() {
        srlMountDetail.isRefreshing = false
    }

    override fun showDetail(mount: Mount?) {
        Glide.with(this).load(mount?.cover).placeholder(R.color.colorMuted).into(imgMountDetail)
        imgMountDetail.onClick { startActivity<ViewPhotoActivity>("photo" to mount?.cover) }

        tvMountName.text = mount?.mountName
        tvMountDesc.text = mount?.description
        tvLocation.text = mount?.location
        tvHeight.text = mount?.height
        tvContact.text = mount?.contact
        tvRoute.text = mount?.route?.replace(", ","\n- ")?.replaceFirst("","- ")

        linkGoogleMaps = mount?.linkGMaps!!

        photo.addAll(mount.gallery?.split("*")!!)
    }

    override fun showUserExperience(experience: List<Experience?>) {
        this.experience.clear()
        this.experience.addAll(experience)
        experienceAdapter.notifyDataSetChanged()
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
            mapboxMap.addOnMapClickListener {
                browse(linkGoogleMaps)
            }
        }
    }

    override fun onMapTouch() {
        placeMap.onTouch { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> scrollView.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP -> scrollView.requestDisallowInterceptTouchEvent(false)
            }
        }
    }

    override fun hideInsta() {
        btnInsta.gone()
    }

    override fun showForecast(forecast: List<Forecast?>) {
        this.forecast.clear()
        this.forecast.addAll(forecast)
        adapter.notifyDataSetChanged()
    }

    override fun showVideo(video: List<SearchResult?>) {
        this.video.clear()
        this.video.addAll(video)
        videoAdapter.notifyDataSetChanged()
    }

    override fun successLike() {
        srlMountDetail.snackbar("Added to the favorites list")
    }

    override fun isLiked() {
        fabFav.imageResource = R.drawable.ic_favorite
        fabFav.onClick { presenter.dislikeMount() }
    }

    override fun isNotLiked() {
        fabFav.imageResource = R.drawable.ic_favorite_border
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.access_token))
        setContentView(R.layout.activity_mount_detail)
        setSupportActionBar(toolbarMountDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mountId = intent.getIntExtra("mountId", 99)

        placeMap.onCreate(savedInstanceState)

        presenter = MountDetailPresenter(this, mountId.toString())
        presenter.getMountDetail()

        setupRecyclerView()
        onAction(mountId)
    }

    private fun setupRecyclerView() {
        adapter = ForecastAdapter(forecast)
        rvForecast.setHasFixedSize(true)
        rvForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvForecast.adapter = adapter
        rvForecast.isNestedScrollingEnabled = false

        experienceAdapter = ExperienceAdapter(experience)
        rvExperience.setHasFixedSize(true)
        rvExperience.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvExperience.adapter = experienceAdapter

        photoAdapter = PhotoAdapter(photo)
        rvPhoto.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPhoto.adapter = photoAdapter

        videoAdapter = VideoAdapter(video)
        rvVideo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvVideo.adapter = videoAdapter
    }

    private fun onAction(id: Int) {
        fabFav.onClick { presenter.likeMount() }
        srlMountDetail.onRefresh { presenter.getMountDetail() }
        btnViewAll.onClick { startActivity<UserExperienceActivity>("id" to id) }
    }

    override fun onDestroy() {
        super.onDestroy()
        placeMap.onDestroy()
        presenter.onClose()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}
