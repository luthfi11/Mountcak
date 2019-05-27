package com.wysiwyg.mountcak.ui.login

import com.google.firebase.auth.FirebaseAuth

class LoginPresenter(private val view: LoginView) {

    private val auth = FirebaseAuth.getInstance()

    fun login(email: String, password: String) {
        view.showLoading()
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                view.hideLoading()
                view.loginSuccess()
            }
            .addOnFailureListener {
                view.hideLoading()
                view.loginFail()
            }
    }
}