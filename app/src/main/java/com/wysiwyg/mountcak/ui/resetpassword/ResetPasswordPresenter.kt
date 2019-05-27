package com.wysiwyg.mountcak.ui.resetpassword

import com.google.firebase.auth.FirebaseAuth

class ResetPasswordPresenter(private val view: ResetPasswordView) {

    private val auth = FirebaseAuth.getInstance()

    fun resetPassword(email: String) {
        view.showLoading()
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                view.hideLoading()
                view.showEmailSent()
            }
            .addOnFailureListener {
                view.hideLoading()
                view.emailNotFound()
            }
    }
}