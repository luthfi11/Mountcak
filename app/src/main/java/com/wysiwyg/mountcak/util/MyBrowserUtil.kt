package com.wysiwyg.mountcak.util

import android.webkit.WebView
import android.webkit.WebViewClient

object MyBrowserUtil : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }
}