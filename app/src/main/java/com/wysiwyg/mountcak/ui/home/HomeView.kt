package com.wysiwyg.mountcak.ui.home

import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.WeatherResponse

interface HomeView {
    fun showLoading()
    fun hideLoading()
    fun showCurrentWeather(weatherResponse: WeatherResponse)
    fun showEventList(event: List<Event?>)
    fun emptyEvent()
    fun notEmptyEvent()
}