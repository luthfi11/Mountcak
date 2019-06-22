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
    fun showOwnPost(event: Event)
    fun isNotOwnPost(uid: String?)
    fun requestSuccess(uid: String?)
    fun requestFailed()
    fun showIsRequested(id: String?)
    fun showIsJoined(id: String?)
    fun showDefault()
    fun cancelDialog(id: String?)
    fun deleteDialog()
    fun eventNotFound()
    fun disableRequest()
}
