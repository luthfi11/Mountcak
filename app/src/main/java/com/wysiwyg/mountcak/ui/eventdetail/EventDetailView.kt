package com.wysiwyg.mountcak.ui.eventdetail

import com.wysiwyg.mountcak.data.model.Event

interface EventDetailView {
    fun showEventDetail(event: Event?)
    fun callNumber(number: String?)
    fun sendMessage(number: String)
    fun viewInstagram(ig: String?)
    fun showMap(long: Double?, lat: Double?, title: String?)
    fun onMapTouch()
    fun hideInsta()
    fun hideCall()
}