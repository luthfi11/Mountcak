package com.wysiwyg.mountcak.ui.mountdetail

import com.wysiwyg.mountcak.data.model.Mount

class MountDetailPresenter(private val view: MountDetailView) {

    fun getMountDetail(mount: Mount?) {
        view.showDetail(mount)

        if (mount?.instagram == null) {
            view.hideInsta()
        }
    }

    fun callNumber(number: String?) {
        if (number != "-")
            view.callNumber(number!!)
        else
            view.hideCall()
    }

    fun sendMessage(number: String?) {
        if (number != "-") {
            view.sendMessage(number!!)
        }
    }

    fun viewPhoto(url: String, title: String) {
        view.showPhoto(url, title)
    }

    fun viewInstagram(ig: String) {
        var url = "https://instagram.com/"

        if (ig != "-") {
            url += ig.replace("@", "")
            view.showInstagram(url)
        } else view.hideInsta()
    }

    fun getMap(longLat: String?, title: String?) {
        val ll = longLat?.split(",")
        view.showMap(ll!![1].toDouble(), ll[0].toDouble(), title)
    }

    fun onMapTouch() {
        view.onMapTouch()
    }
}