package com.wysiwyg.mountcak.ui.userdetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.ui.home.EventAdapter
import com.wysiwyg.mountcak.ui.viewphoto.ViewPhotoActivity
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class UserDetailActivity : AppCompatActivity(), UserDetailView {

    private lateinit var presenter: UserDetailPresenter
    private lateinit var adapter: EventAdapter
    private val event: MutableList<Event?> = mutableListOf()

    override fun showLoading() {
        progressUser.visible()
        rvUserEvent.gone()
    }

    override fun hideLoading() {
        progressUser.gone()
        rvUserEvent.visible()
    }

    override fun showUserData(user: User?) {
        Glide.with(this).load(user?.photo).placeholder(R.color.colorMuted).into(imgAva)

        tvName.text = user?.name
        tvCity.text = user?.city

        imgAva.onClick { startActivity<ViewPhotoActivity>("photo" to user?.photo) }
    }

    override fun showUserPost(event: MutableList<Event?>) {
        this.event.clear()
        this.event.addAll(event)
        adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        setSupportActionBar(toolbarUser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = EventAdapter(event)

        rvUserEvent.setHasFixedSize(true)
        rvUserEvent.layoutManager = LinearLayoutManager(this)
        rvUserEvent.adapter = adapter

        val uid = intent.getStringExtra("userId")
        presenter = UserDetailPresenter(this)
        presenter.getUserData(uid)
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