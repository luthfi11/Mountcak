package com.wysiwyg.mountcak.ui.notification

import com.wysiwyg.mountcak.data.model.Join

interface NotificationView {
    fun showLoading()
    fun hideLoading()
    fun showNotification(join : List<Join?>)
    fun emptyNotification()
    fun notEmptyNotification()
}