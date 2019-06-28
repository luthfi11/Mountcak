package com.wysiwyg.mountcak.ui.rentaldetail

import com.wysiwyg.mountcak.data.model.Rental

interface RentalDetailView {
    fun showRentalDetail(rental: Rental?)
    fun callNumber(number: String?)
    fun sendMessage(number: String?)
    fun viewInstagram(id: String?)
    fun showMap(long: Double?, lat: Double?, title: String?)
    fun onMapTouch()
    fun openGoogleMaps(link: String?)
    fun hideContact()
    fun hideInstagram()
    fun hidePriceList()
}