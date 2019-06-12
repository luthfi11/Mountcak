package com.wysiwyg.mountcak.ui.editevent

import com.wysiwyg.mountcak.data.model.Event

interface EditEventView {
    fun showData(event: Event?)
    fun showLoading()
    fun hideLoading()
    fun updateSuccess()
    fun updateFailed()
}