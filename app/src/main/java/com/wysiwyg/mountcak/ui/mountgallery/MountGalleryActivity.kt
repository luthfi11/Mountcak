package com.wysiwyg.mountcak.ui.mountgallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.wysiwyg.mountcak.R
import kotlinx.android.synthetic.main.activity_mount_gallery.*

class MountGalleryActivity : AppCompatActivity(), MountGalleryView {

    private lateinit var presenter: MountGalleryPresenter
    private lateinit var adapter: GalleryAdapter
    private val photo = mutableListOf<String?>()

    override fun showPhotos(photo: List<String?>) {
        this.photo.clear()
        this.photo.addAll(photo)
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mount_gallery)
        setSupportActionBar(toolbarGallery)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        val title = intent.getStringExtra("title")
        val listPhoto = intent.getStringExtra("photo").split("*")

        tvMountName.text = title
        setupRecyclerView()

        presenter = MountGalleryPresenter(this)
        presenter.getPhotos(listPhoto)
    }

    private fun setupRecyclerView() {
        adapter = GalleryAdapter(photo)
        rvGallery.setHasFixedSize(true)
        rvGallery.layoutManager = LinearLayoutManager(this)
        rvGallery.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}
