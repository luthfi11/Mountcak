package com.wysiwyg.mountcak.ui.profile

import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.User

interface ProfileView {
    fun showLoading()
    fun hideLoading()
    fun showData(user: User?)
    fun showEvent(event: List<Event?>)
    fun showTrip(event: List<Event?>)
    fun showMount(mount: List<Mount?>)
    fun doLogout()
}