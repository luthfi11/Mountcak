package com.wysiwyg.mountcak.ui.home

import com.wysiwyg.mountcak.data.model.Event

interface HomeView {
    fun showLoading()
    fun hideLoading()
    fun showEventList(event: MutableList<Event?>)
}