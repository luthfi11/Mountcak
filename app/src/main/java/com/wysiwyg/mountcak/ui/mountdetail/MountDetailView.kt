package com.wysiwyg.mountcak.ui.mountdetail

import com.wysiwyg.mountcak.data.model.Review
import com.wysiwyg.mountcak.data.model.Forecast
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.SearchResult

interface MountDetailView {
    fun showLoading()
    fun hideLoading()
    fun showDetail(mount: Mount?)
    fun showUserReview(review: List<Review?>)
    fun emptyReview()
    fun callNumber(number: String)
    fun sendMessage(number: String)
    fun hideCall()
    fun showInstagram(ig: String)
    fun showMap(long: Double?, lat: Double?, title: String?)
    fun onMapTouch()
    fun hideInsta()
    fun showForecast(forecast: List<Forecast?>)
    fun showVideo(video: List<SearchResult?>)
    fun successLike()
    fun isLiked()
    fun isNotLiked()
    fun showForecastLoading()
    fun showVideoLoading()
    fun hideForecastLoading()
    fun hideVideoLoading()
}