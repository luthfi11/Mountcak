package com.wysiwyg.mountcak.ui.mountdetail

import com.wysiwyg.mountcak.data.model.Mount

interface MountDetailView {
    fun showLoading()
    fun hideLoading()
    fun showDetail(mount: Mount?)
    fun callNumber(number: String)
    fun sendMessage(number: String)
    fun hideCall()
    fun showPhoto(title: String?, photo: String?)
    fun showInstagram(ig: String)
    fun showMap(long: Double?, lat: Double?, title: String?)
    fun onMapTouch()
    fun hideInsta()
    fun isLiked()
    fun isNotLiked()
}