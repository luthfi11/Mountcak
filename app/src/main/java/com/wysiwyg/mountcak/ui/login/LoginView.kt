package com.wysiwyg.mountcak.ui.login

interface LoginView {
    fun showLoading()
    fun hideLoading()
    fun loginSuccess()
    fun loginFail()
}