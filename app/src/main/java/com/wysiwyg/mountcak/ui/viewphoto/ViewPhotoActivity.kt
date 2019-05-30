package com.wysiwyg.mountcak.ui.viewphoto

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import kotlinx.android.synthetic.main.activity_view_photo.*

class ViewPhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_photo)
        setSupportActionBar(toolbarPhoto)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val photo = intent.getStringExtra("photo")
        Glide.with(this).load(photo).into(imgPhoto)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
