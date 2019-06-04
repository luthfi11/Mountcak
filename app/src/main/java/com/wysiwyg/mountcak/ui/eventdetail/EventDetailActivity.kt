package com.wysiwyg.mountcak.ui.eventdetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event

class EventDetailActivity : AppCompatActivity(), EventDetailView {

    private lateinit var presenter: EventDetailPresenter
    private lateinit var event: Event

    override fun showEventDetail(event: Event?) {

    }

    override fun callNumber(number: String?) {

    }

    override fun sendMessage(number: String) {

    }

    override fun viewInstagram(ig: String?) {

    }

    override fun showMap(long: Double?, lat: Double?, title: String?) {

    }

    override fun onMapTouch() {

    }

    override fun hideInsta() {

    }

    override fun hideCall() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        event = intent.getParcelableExtra("event")

        presenter = EventDetailPresenter(this)
        presenter.getDetail(event)
    }
}
