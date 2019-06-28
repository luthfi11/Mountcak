package com.wysiwyg.mountcak.ui.eventdetail

import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.User
import java.util.*

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
    fun showJoinedUser(user: List<User?>)
    fun cancelDialog(id: String?)
    fun deleteDialog()
    fun eventNotFound()
    fun disableRequest()
    fun addToCalendar(event: Event?, dateStart: GregorianCalendar, dateEnd: GregorianCalendar)
}
