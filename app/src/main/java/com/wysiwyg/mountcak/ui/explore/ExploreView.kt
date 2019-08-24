package com.wysiwyg.mountcak.ui.explore

import com.wysiwyg.mountcak.data.model.Mount

interface ExploreView {
    fun showLoading()
    fun hideLoading()
    fun showRegionList(region: List<String?>)
    fun showMountList(mount: List<Mount?>)
}