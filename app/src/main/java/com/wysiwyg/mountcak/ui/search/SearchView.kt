package com.wysiwyg.mountcak.ui.search

import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.Rental

interface SearchView {
    fun showLoading()
    fun hideLoading()
    fun showMountData(mount: List<Mount?>)
    fun showRentalData(rental: List<Rental?>)
    fun showEmpty()
    fun showNotEmpty()
}