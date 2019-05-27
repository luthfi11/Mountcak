package com.wysiwyg.mountcak.ui.profile

interface ProfileView {
    fun showLoading()
    fun hideLoading()
    fun showData(name: String?, city: String?)
}