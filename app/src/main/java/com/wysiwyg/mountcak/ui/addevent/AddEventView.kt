package com.wysiwyg.mountcak.ui.addevent

interface AddEventView {
    fun showMountList(name: List<String?>, location: List<String?>)
    fun setID(eid: String?, uid: String?)
    fun showLoading()
    fun hideLoading()
    fun successPost()
    fun postingFail()
}