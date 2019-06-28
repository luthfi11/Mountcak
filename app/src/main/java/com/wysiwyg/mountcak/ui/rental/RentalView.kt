package com.wysiwyg.mountcak.ui.rental

import com.wysiwyg.mountcak.data.model.Rental

interface RentalView {
    fun showLoading()
    fun hideLoading()
    fun showData(rental: List<Rental?>)
}