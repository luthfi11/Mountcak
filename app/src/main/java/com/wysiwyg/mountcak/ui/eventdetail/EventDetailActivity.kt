package com.wysiwyg.mountcak.ui.eventdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.ui.chatroom.ChatRoomActivity
import com.wysiwyg.mountcak.ui.editevent.EditEventActivity
import com.wysiwyg.mountcak.ui.userdetail.UserDetailActivity
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.invisible
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.activity_event_detail.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class EventDetailActivity : AppCompatActivity(), EventDetailView {

    private lateinit var presenter: EventDetailPresenter
    private lateinit var eid: String
    private var postSender: String? = null
    private var mountName: String? = null

    override fun showLoading() {
        progressEvent.visible()
        eventContent.gone()
    }

    override fun hideLoading() {
        progressEvent.gone()
        eventContent.visible()
    }

    override fun showEventDetail(event: Event?) {
        postSender = event?.userId

        tvEventTitle.text = event?.title
        tvEventNote.text = event?.eventNote
        tvMeetLoc.text = event?.meetLocation
        tvDate.text = presenter.checkDate(event?.dateStart, event?.dateEnd)
        tvMaxPar.text = String.format(getString(R.string.participant_count), event?.maxParticipant)

        presenter.checkCost(event?.cost, tvCost)
    }

    override fun showMountData(mount: Mount?) {
        mountName = mount?.mountName

        Glide.with(this).load(mount?.cover).placeholder(R.color.colorMuted).into(imgMount)
        tvMountName.text = mount?.mountName
        tvLocation.text = mount?.location
    }

    override fun showUserData(user: User?) {
        Glide.with(this).load(user?.photo).placeholder(R.color.colorMuted).into(imgUser)
        tvUsername.text = user?.name
    }

    override fun showOwnPost(event: Event) {
        lytEventEdit.visible()
        lytEventJoin.invisible()
        btnEdit.onClick { startActivity<EditEventActivity>("event" to event) }
        btnDelete.onClick { deleteDialog() }
    }

    override fun isNotOwnPost(uid: String?) {
        lytEventEdit.gone()
        lytEventJoin.visible()
        imgUser.onClick { startActivity<UserDetailActivity>("userId" to uid) }
        btnChat.onClick { startActivity<ChatRoomActivity>("userId" to uid) }
    }

    override fun requestSuccess(uid: String?) {
        startActivity<ChatRoomActivity>("userId" to postSender)
    }

    override fun requestFailed() {
        toast("Failed to request to join this event. Please try again !")
    }

    override fun showIsRequested(id: String?) {
        btnRequestJoin.textResource = R.string.requested
        btnRequestJoin.textColorResource = android.R.color.black
        btnRequestJoin.backgroundResource = R.drawable.shape_button_white
        btnRequestJoin.onClick { presenter.cancelRequest(id) }
    }

    override fun showIsJoined(id: String?) {
        btnRequestJoin.onClick { cancelDialog(id) }
    }

    override fun showDefault() {
        btnRequestJoin.textResource = R.string.request_join
        btnRequestJoin.textColorResource = R.color.colorPrimary
        btnRequestJoin.backgroundResource = R.drawable.shape_button_white
        btnRequestJoin.onClick { presenter.requestJoin(postSender, mountName) }
    }

    override fun cancelDialog(id: String?) {
        alert {
            titleResource = R.string.cancel_join
            isCancelable = false
            yesButton { presenter.cancelRequest(id) }
            noButton { it.dismiss() }
        }.show()
    }

    override fun deleteDialog() {
        alert {
            titleResource = R.string.delete_hint
            isCancelable = false
            yesButton { presenter.deletePost() }
            noButton { it.dismiss() }
        }.show()
    }

    override fun eventNotFound() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        setSupportActionBar(toolbarEventDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        eid = intent.getStringExtra("eid")

        presenter = EventDetailPresenter(this, eid)
        presenter.getEventDetail()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onClose()
    }
}
