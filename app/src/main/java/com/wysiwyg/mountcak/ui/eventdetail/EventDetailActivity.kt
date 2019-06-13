package com.wysiwyg.mountcak.ui.eventdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.ui.editevent.EditEventActivity
import com.wysiwyg.mountcak.ui.userdetail.UserDetailActivity
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class EventDetailActivity : AppCompatActivity(), EventDetailView {

    private lateinit var presenter: EventDetailPresenter
    private lateinit var eid: String

    override fun showLoading() {
        progressEvent.visible()
        eventContent.gone()
    }

    override fun hideLoading() {
        progressEvent.gone()
        eventContent.visible()
    }

    override fun showEventDetail(event: Event?) {
        tvEventTitle.text = event?.title
        tvEventNote.text = event?.eventNote
        tvMeetLoc.text = event?.meetLocation
        tvDate.text = presenter.checkDate(event?.dateStart, event?.dateEnd)
        tvMaxPar.text = String.format(getString(R.string.participant_count), event?.maxParticipant)

        presenter.checkCost(event?.cost, tvCost)
    }

    override fun showMountData(mount: Mount?) {
        Glide.with(this).load(mount?.cover).placeholder(R.color.colorMuted).into(imgMount)
        tvMountName.text = mount?.mountName
        tvLocation.text = mount?.location
    }

    override fun showUserData(user: User?) {
        Glide.with(this).load(user?.photo).placeholder(R.color.colorMuted).into(imgUser)
        tvUsername.text = user?.name
    }

    override fun showOwnPost(event: Event) {
        btnEdit.visible()
        btnEdit.onClick { startActivity<EditEventActivity>("event" to event) }
    }

    override fun canViewProfile(uid: String?) {
        btnEdit.gone()
        imgUser.onClick { startActivity<UserDetailActivity>("userId" to uid) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        setSupportActionBar(toolbarEventDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        eid = intent.getStringExtra("eid")

        presenter = EventDetailPresenter(this)
        presenter.getEventDetail(eid)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onClose(eid)
    }
}
