package com.wysiwyg.mountcak.ui.eventdetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.ui.userdetail.UserDetailActivity
import com.wysiwyg.mountcak.util.DateUtil
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class EventDetailActivity : AppCompatActivity(), EventDetailView {

    private lateinit var presenter: EventDetailPresenter
    private lateinit var event: Event

    override fun showEventDetail(event: Event?) {
        tvEventTitle.text = event?.title
        tvEventNote.text = event?.eventNote
        tvMeetLoc.text = event?.meetLocation
        tvDate.text = DateUtil.dateFormat(event?.dateStart, "EEEE, dd MMMM") + " - " + DateUtil.dateFormat(event?.dateEnd, "EEEE, dd MMMM yyyy")
        tvMaxPar.text = "${event?.maxParticipant} Participants"
        tvCost.text = "IDR ${event?.cost}"

        presenter.mountData(event?.mountId!!)
        presenter.userData(event.userId!!)

        imgUser.onClick { startActivity<UserDetailActivity>("userId" to event.userId) }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        setSupportActionBar(toolbarEventDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        event = intent.getParcelableExtra("event")

        presenter = EventDetailPresenter(this)
        presenter.getDetail(event)
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
