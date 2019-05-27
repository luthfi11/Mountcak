package com.wysiwyg.mountcak.ui.home

import com.wysiwyg.mountcak.data.model.Mount

interface HomeView {
    fun showLoading()
    fun hideLoading()
    fun showMountList(mount: MutableList<Mount?>)
}