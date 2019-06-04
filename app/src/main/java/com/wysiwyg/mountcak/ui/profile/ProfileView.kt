package com.wysiwyg.mountcak.ui.profile

import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.User

interface ProfileView {
    fun showLoading()
    fun hideLoading()
    fun showData(user: User?)
    fun showEvent(event: MutableList<Event?>)
    fun doLogout()
}