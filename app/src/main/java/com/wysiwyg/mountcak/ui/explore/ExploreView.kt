package com.wysiwyg.mountcak.ui.explore

import com.wysiwyg.mountcak.data.model.Mount

interface ExploreView {
    fun showLoading()
    fun hideLoading()
    fun showMountList(mount: List<Mount?>)
}