package com.wysiwyg.mountcak.ui.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    val ifSuccess by lazy { MutableLiveData<Boolean>() }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                ifSuccess.postValue(true)
            }
            .addOnFailureListener {
                ifSuccess.postValue(false)
            }
    }
}