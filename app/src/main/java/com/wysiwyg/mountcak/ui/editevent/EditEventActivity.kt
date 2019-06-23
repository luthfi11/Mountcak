package com.wysiwyg.mountcak.ui.editevent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.util.DateUtil
import com.wysiwyg.mountcak.util.LoadingDialog
import com.wysiwyg.mountcak.util.ValidateUtil.etToInt
import com.wysiwyg.mountcak.util.ValidateUtil.etToString
import com.wysiwyg.mountcak.util.ValidateUtil.spnPosition
import com.wysiwyg.mountcak.util.ValidateUtil.validate
import com.wysiwyg.mountcak.util.showMountList
import kotlinx.android.synthetic.main.activity_add_event.*
import org.jetbrains.anko.textResource
import org.jetbrains.anko.toast

class EditEventActivity: AppCompatActivity(), EditEventView, View.OnClickListener {

    private lateinit var presenter: EditEventPresenter
    private lateinit var event: Event

    override fun showData(event: Event?) {
        etTitle.setText(event?.title)
        etEventDesc.setText(event?.eventNote)
        etDateStart.setText(event?.dateStart)
        etDateEnd.setText(event?.dateEnd)
        etCost.setText(event?.cost.toString())
        etMaxParticipant.setText(event?.maxParticipant.toString())
        etMeetLocation.setText(event?.meetLocation)

        showMountList(this, spnMount, tvMountLocation,event?.mountId!!.toInt())
    }

    override fun showLoading() {
        LoadingDialog.showLoading(this, "Updating Post")
    }

    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }

    override fun updateSuccess() {
        toast("Post Updated")
        finish()
    }

    override fun updateFailed() {
        toast("Failed to update event, Please try again !")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnDateStart -> DateUtil.datePicker(etDateStart, this, (System.currentTimeMillis() - 1000))
            R.id.btnDateEnd -> DateUtil.datePicker(etDateEnd, this, DateUtil.dateToLong(etToString(etDateStart)))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        setSupportActionBar(toolbarAddEvent)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        tvTitle.textResource = R.string.edit_post

        event = intent.getParcelableExtra("event")

        presenter = EditEventPresenter(this)
        presenter.getData(event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        else if (item?.itemId == R.id.nav_done) {
            if (validate(etMeetLocation, getString(R.string.general_validate)) and
                validate(etMaxParticipant, getString(R.string.general_validate)) and
                validate(etDateEnd, getString(R.string.general_validate)) and
                validate(etDateStart, getString(R.string.general_validate)) and
                validate(etTitle, getString(R.string.general_validate))
            ) {
                presenter.updateData(
                    event.id!!,
                    Event(
                        event.id,
                        etToString(etTitle),
                        event.userId,
                        spnPosition(spnMount),
                        etToString(etDateStart),
                        etToString(etDateEnd),
                        etToString(etEventDesc),
                        etToInt(etCost),
                        etToInt(etMaxParticipant),
                        etToString(etMeetLocation)
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

}