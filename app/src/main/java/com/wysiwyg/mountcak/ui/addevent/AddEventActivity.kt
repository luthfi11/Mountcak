package com.wysiwyg.mountcak.ui.addevent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.util.DateUtil
import com.wysiwyg.mountcak.util.DateUtil.dateToLong
import com.wysiwyg.mountcak.util.LoadingDialog
import com.wysiwyg.mountcak.util.ValidateUtil.etToInt
import com.wysiwyg.mountcak.util.ValidateUtil.etToString
import com.wysiwyg.mountcak.util.ValidateUtil.spnPosition
import com.wysiwyg.mountcak.util.ValidateUtil.validate
import kotlinx.android.synthetic.main.activity_add_event.*
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.toast

class AddEventActivity : AppCompatActivity(), AddEventView, View.OnClickListener {

    private lateinit var presenter: AddEventPresenter
    private var eid: String? = null
    private var uid: String? = null

    override fun setID(eid: String?, uid: String?) {
        this.eid = eid
        this.uid = uid
    }

    override fun showLoading() {
        LoadingDialog.showLoading(this, R.string.posting)
    }

    override fun hideLoading() {
        LoadingDialog.hideLoading()
    }

    override fun successPost() {
        finish()
        toast(R.string.event_posted)
    }

    override fun postingFail() {
        toast(R.string.posting_fail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        setSupportActionBar(toolbarAddEvent)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        presenter = AddEventPresenter(this)
        presenter.getMountList(this, spnMount, tvMountLocation)
        presenter.setID()

        valDate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        else if (item?.itemId == R.id.nav_done) post()
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnDateStart -> DateUtil.datePicker(etDateStart, this, (System.currentTimeMillis() - 1000))
            R.id.btnDateEnd -> DateUtil.datePicker(etDateEnd, this, dateToLong(etToString(etDateStart)))
        }
    }

    private fun post() {
        if (validate(etMeetLocation, getString(R.string.general_validate)) and
            validate(etMaxParticipant, getString(R.string.general_validate)) and
            validate(etDateEnd, getString(R.string.general_validate)) and
            validate(etDateStart, getString(R.string.general_validate)) and
            validate(etTitle, getString(R.string.general_validate))
        ) {
            presenter.postEvent(
                Event(
                    eid,
                    etToString(etTitle),
                    uid,
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

    private fun valDate() {
        if (etDateStart.text.isEmpty()) {
            btnDateEnd.isEnabled = false
            btnDateEnd.textColorResource = R.color.colorMuted
        }
        etDateStart.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnDateEnd.isEnabled = true
                btnDateEnd.textColorResource = R.color.colorPrimary
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }
}
