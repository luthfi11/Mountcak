package com.wysiwyg.mountcak.ui.userdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import bold
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.ui.chatroom.ChatRoomActivity
import com.wysiwyg.mountcak.ui.home.EventAdapter
import com.wysiwyg.mountcak.ui.viewphoto.ViewPhotoActivity
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import plus
import size
import spannable

class UserDetailActivity : AppCompatActivity(), UserDetailView, TabLayout.OnTabSelectedListener {

    private lateinit var presenter: UserDetailPresenter
    private lateinit var adapter: EventAdapter
    private lateinit var uid: String
    private val event: MutableList<Event?> = mutableListOf()
    private val trip: MutableList<Event?> = mutableListOf()

    override fun showLoading() {
        progressUser.visible()
        rvProfile.gone()
    }

    override fun hideLoading() {
        progressUser.gone()
        rvProfile.visible()
    }

    override fun showUserData(user: User?) {
        Glide.with(this).load(user?.photo).placeholder(R.color.colorMuted).into(imgAva)

        tvName.text = user?.name
        tvCity.text = user?.city

        imgAva.onClick { startActivity<ViewPhotoActivity>("photo" to user?.photo) }
    }

    override fun showUserPost(event: List<Event?>) {
        this.event.clear()
        this.event.addAll(event)
        adapter.notifyDataSetChanged()

        tabs.getTabAt(0)?.text = spannable { bold(size(1.4F, "${event.size}")) + "\nPost" }
    }

    override fun showUserTrip(event: List<Event?>) {
        trip.clear()
        trip.addAll(event)
        adapter.notifyDataSetChanged()

        tabs.getTabAt(1)?.text = spannable { bold(size(1.4F, "${event.size}")) + "\nTrip" }
    }

    override fun emptyList(title: String) {
        rvProfile.gone()
        tvEmpty.visible()
        tvEmpty.text = title
    }

    override fun notEmptyList() {
        rvProfile.visible()
        tvEmpty.gone()
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        when (p0?.position) {
            0 -> {
                adapter = EventAdapter(event)
                rvProfile.adapter = adapter

                if (event.isEmpty()) emptyList("No Post Yet")
                else notEmptyList()
            }
            1 -> {
                adapter = EventAdapter(trip)
                rvProfile.adapter = adapter

                if (trip.isEmpty()) emptyList("No Trip Yet")
                else notEmptyList()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        setSupportActionBar(toolbarUser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        uid = intent.getStringExtra("userId")
        presenter = UserDetailPresenter(this, uid)
        presenter.getUserData()

        setupRecyclerView()
        tabs.addOnTabSelectedListener(this)
        btnChat.onClick { startActivity<ChatRoomActivity>("userId" to uid) }
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(event)
        rvProfile.setHasFixedSize(true)
        rvProfile.layoutManager = LinearLayoutManager(this)
        rvProfile.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        presenter.getUserData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_refresh, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        else if (item?.itemId == R.id.nav_refresh) presenter.getUserData()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onClose()
    }
}
