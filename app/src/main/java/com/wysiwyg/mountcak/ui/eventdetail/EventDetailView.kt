package com.wysiwyg.mountcak.ui.eventdetail

import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.User

interface EventDetailView {
    fun showLoading()
    fun hideLoading()
    fun showEventDetail(event: Event?)
    fun showMountData(mount: Mount?)
    fun showUserData(user: User?)
    fun showEditButton(event: Event)
}