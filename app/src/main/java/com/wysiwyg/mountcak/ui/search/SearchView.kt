package com.wysiwyg.mountcak.ui.search

import com.wysiwyg.mountcak.data.model.Mount

interface SearchView {
    fun showLoading()
    fun hideLoading()
    fun showData(data: MutableList<Mount?>)
}