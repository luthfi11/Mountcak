package com.wysiwyg.mountcak.ui.eventdetail

import com.wysiwyg.mountcak.data.model.Event

class EventDetailPresenter(private val view: EventDetailView) {

    fun getDetail(event: Event?) {
        view.showEventDetail(event)
    }
}