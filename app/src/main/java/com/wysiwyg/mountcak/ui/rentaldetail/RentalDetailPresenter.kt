package com.wysiwyg.mountcak.ui.rentaldetail

import com.wysiwyg.mountcak.data.model.Rental

class RentalDetailPresenter(private val view: RentalDetailView, private val rental: Rental?) {

    fun getRentalDetail() {
        view.showRentalDetail(rental)
        contactEmpty()
        instagramEmpty()
        priceEmpty()
        showMap()
    }

    fun callNumber() {
        view.callNumber(rental?.contact)
    }

    fun sendMessage() {
        view.sendMessage(rental?.contact)
    }

    fun viewInstagram() {
        view.viewInstagram(rental?.instagram)
    }

    fun openGoogleMaps() {
        view.openGoogleMaps(rental?.linkMap)
    }

    private fun contactEmpty() {
        if (rental?.contact == "") view.hideContact()
    }

    private fun instagramEmpty() {
        if (rental?.instagram == "") view.hideInstagram()
    }

    private fun priceEmpty() {
        if (rental?.priceList == "") view.hidePriceList()
    }

    private fun showMap() {
        val ll = rental?.longLat?.split(",")
        view.showMap(ll!![1].toDouble(), ll[0].toDouble(), rental?.storeName)
        view.onMapTouch()
    }
}