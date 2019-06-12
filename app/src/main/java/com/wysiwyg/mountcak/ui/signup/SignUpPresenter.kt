package com.wysiwyg.mountcak.ui.signup

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.wysiwyg.mountcak.data.model.User

class SignUpPresenter(private val view: SignUpView) {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    fun getCity() {
        view.showCity()
    }

    fun signUp(email: String, password: String, name: String, city: String) {
        view.showLoading()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                view.hideLoading()
                auth.currentUser?.sendEmailVerification()
                db.child("user")
                    .child(auth.currentUser!!.uid)
                    .setValue(
                        User(
                            auth.currentUser!!.uid,
                            name,
                            city,
                            email
                        )
                    )
                view.signUpSuccess()
            }
            .addOnFailureListener {
                view.hideLoading()
                view.signUpFail()
            }
    }
}