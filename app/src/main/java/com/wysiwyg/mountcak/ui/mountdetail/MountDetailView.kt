package com.wysiwyg.mountcak.ui.mountdetail

import com.wysiwyg.mountcak.data.model.Mount

interface MountDetailView {
    fun showDetail(mount: Mount?)
    fun callNumber(number: String)
    fun sendMessage(number: String)
    fun hideCall()
    fun showPhoto(url: String, title: String)
    fun showInstagram(ig: String)
    fun showMap(long: Double?, lat: Double?, title: String?)
    fun onMapTouch()
    fun hideInsta()
}