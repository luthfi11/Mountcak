package com.wysiwyg.mountcak.ui.resetpassword

interface ResetPasswordView {
    fun showLoading()
    fun hideLoading()
    fun showEmailSent()
    fun emailNotFound()
}