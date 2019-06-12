package com.wysiwyg.mountcak.ui.addevent

interface AddEventView {
    fun setID(eid: String?, uid: String?)
    fun showLoading()
    fun hideLoading()
    fun successPost()
    fun postingFail()
}