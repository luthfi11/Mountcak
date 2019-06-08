package com.wysiwyg.mountcak.ui.userdetail

import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.User

interface UserDetailView {
    fun showLoading()
    fun hideLoading()
    fun showUserData(user: User?)
    fun showUserPost(event: MutableList<Event?>)
}