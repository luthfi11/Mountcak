package com.wysiwyg.mountcak.ui.addevent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.util.DateUtil
import com.wysiwyg.mountcak.util.LoadingDialog
import com.wysiwyg.mountcak.util.ValidateUtil.etToInt
import com.wysiwyg.mountcak.util.ValidateUtil.etToString
import com.wysiwyg.mountcak.util.ValidateUtil.spnPosition
import com.wysiwyg.mountcak.util.ValidateUtil.validate
import kotlinx.android.synthetic.main.activity_add_event.*
import org.jetbrains.anko.toast

class AddEventActivity : AppCompatActivity(), AddEventView, View.OnClickListener {

    private lateinit var presenter: AddEventPresenter
    private var eid: String? = null
    private var uid: String? = null

    override fun showMountList(name: List<String?>, location: List<String?>) {
        spnMount.adapter = ArrayAdapter(this, R.layout.spinner_item, name)
        spnMount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tvMountLocation.text = location[position]
            }
        }
    }

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

        presenter = AddEventPresenter(this)
        presenter.getMountList()
        presenter.setID()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.nav_done -> {
                post()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnDateStart -> DateUtil.datePicker(etDateStart, this)
            R.id.btnDateEnd -> DateUtil.datePicker(etDateEnd, this)
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
}
